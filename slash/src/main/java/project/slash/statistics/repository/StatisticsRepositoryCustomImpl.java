package project.slash.statistics.repository;

import static project.slash.contract.model.QEvaluationItem.*;
import static project.slash.statistics.model.QStatistics.*;
import static project.slash.system.model.QEquipment.*;
import static project.slash.system.model.QSystems.*;
import static project.slash.systemincident.model.QSystemIncident.*;
import static project.slash.taskrequest.model.QTaskRequest.*;
import static project.slash.taskrequest.model.QTaskType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.dto.response.ResponseServiceTaskDto;
import project.slash.taskrequest.model.constant.RequestStatus;

@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryCustomImpl implements StatisticsRepositoryCustom {
	@Autowired
	private final JPAQueryFactory queryFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<MonthlyDataDto> getMonthlyData() {
		StringTemplate yearMonth = Expressions.stringTemplate(
			"DATE_FORMAT({0}, '%Y-%m')",
			taskRequest.createTime);

		LocalDate previousMonthDate = LocalDate.now().minusMonths(1);
		String previousMonth = previousMonthDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));

		return queryFactory
			.select(Projections.constructor(MonthlyDataDto.class,
				yearMonth,
				systems.name,
				equipment.name,
				taskRequest.count(),
				systemIncident.incidentTime.sum(),
				Expressions.numberTemplate(Integer.class, "DAY(LAST_DAY({0}))", taskRequest.createTime),
				systemIncident.count()
			))
			.from(taskRequest)
			.leftJoin(equipment).on(taskRequest.equipment.id.eq(equipment.id))
			.leftJoin(systems).on(equipment.systems.id.eq(systems.id))
			.leftJoin(systemIncident).on(systemIncident.taskRequest.id.eq(taskRequest.id))
			.leftJoin(taskType).on(taskRequest.taskType.id.eq(taskType.id))// 현재 년-월보다 이전 것만 필터링
			.where(yearMonth.eq(previousMonth))
			.groupBy(equipment.name)
			.orderBy(systems.name.asc(), equipment.name.asc())
			.fetch();
	}

	@Override
	public void saveMonthlyData(List<MonthlyServiceStatisticsDto> statsDtoList) {
		String sql = "INSERT INTO statistics (`date`, service_type, grade, score, period, weighted_score, " +
			"approval_status, total_downtime, request_count, evaluation_item_id, target_system, estimate,system_incident_count,due_on_time_count,target_equipment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

		jdbcTemplate.batchUpdate(sql, statsDtoList, 50, (ps, dto) -> {
			ps.setDate(1, java.sql.Date.valueOf(dto.getDate()));
			ps.setString(2, dto.getServiceType());
			ps.setString(3, dto.getGrade());
			ps.setDouble(4, dto.getScore());
			ps.setString(5, dto.getPeriod());
			ps.setDouble(6, dto.getWeightedScore());
			ps.setBoolean(7, dto.isApprovalStatus());
			ps.setLong(8, dto.getTotalDowntime());
			ps.setLong(9, dto.getRequestCount());
			ps.setLong(10, dto.getEvaluationItemId());
			ps.setString(11, dto.getTargetSystem());
			ps.setDouble(12, dto.getEstimate());
			ps.setLong(13, dto.getSystemIncidentCount());
			ps.setLong(14, dto.getDueOnTimeCount());
			ps.setString(15, dto.getTargetEquipment());
		});
	}

	@Override
	public List<StatisticsDto> getStatistics(String serviceType, String period, String targetSystem,
		String targetEquipment) {
		BooleanBuilder builder = new BooleanBuilder();

		// 매개변수가 null이 아닐 때만 조건 추가
		if (serviceType != null) {
			builder.and(statistics.serviceType.eq(serviceType));
		}
		if (period != null) {
			builder.and(statistics.period.eq(period));
		}
		if (targetSystem != null) {
			builder.and(statistics.targetSystem.eq(targetSystem));
		}
		if (targetEquipment != null) {
			builder.and(statistics.targetEquipment.eq(targetEquipment));
		}

		// 쿼리 실행

		return queryFactory
			.select(Projections.constructor(StatisticsDto.class, // DTO로 변환
				statistics.date,
				statistics.serviceType,
				statistics.grade,
				statistics.score,
				statistics.totalDowntime,
				statistics.requestCount,
				statistics.dueOnTimeCount,
				statistics.targetSystem,
				statistics.targetEquipment))
			.from(statistics)
			.where(builder)
			.fetch();
	}

	@Override
	public ResponseServiceTaskDto getServiceTaskStatics(RequestStatisticsDto requestStatisticsDto) {

		LocalDateTime startDate = requestStatisticsDto.getDate()
			.withDayOfMonth(1)
			.atTime(0, 0, 0);  // 시작일은 endDate의 첫 번째 날
		LocalDateTime endDate = requestStatisticsDto.getDate().atTime(23, 59, 59);

		// evaluationItemId의 contractId 가져오기
		Long contractId = new JPAQuery<>(entityManager)
			.select(evaluationItem.contract.id)
			.from(evaluationItem)
			.where(evaluationItem.id.eq(requestStatisticsDto.getEvaluationItemId()))
			.fetchOne();

		// totalWeight 서브쿼리
		NumberExpression<Integer> totalWeightSubquery = Expressions.numberTemplate(
			Integer.class,
			"({0})",
			JPAExpressions
				.select(evaluationItem.weight.sum())  // contract_id에 대한 weight 합계 계산
				.from(evaluationItem)
				.where(evaluationItem.contract.id.eq(contractId))
		);

		//요청 완료 건수 서브 쿼리
		NumberTemplate<Integer> dueOnTimeCountSubquery = Expressions.numberTemplate(
			Integer.class,
			"({0})",
			JPAExpressions
				.select(taskRequest.status.count())  // 완료된 요청을 카운트
				.from(taskRequest)
				.where(taskRequest.status.eq(RequestStatus.COMPLETED))
				.where(taskRequest.createTime.between(startDate, endDate))
				.where(taskRequest.taskType.evaluationItem.id.eq(requestStatisticsDto.getEvaluationItemId()))
		);

		return queryFactory
			.select(Projections.constructor(
				ResponseServiceTaskDto.class,
				evaluationItem.as("evaluationItem"), // evaluationItemId
				taskRequest.count().as("taskRequest"), // 전체 요청 수
				totalWeightSubquery,
				dueOnTimeCountSubquery
			))
			.from(taskRequest)
			.leftJoin(taskRequest.taskType, taskType)
			.leftJoin(taskType.evaluationItem, evaluationItem)
			.where(taskRequest.createTime.between(startDate, endDate))
			.where(evaluationItem.id.eq(requestStatisticsDto.getEvaluationItemId()))
			.fetchOne(); // ResponseServiceTaskDto 타입으로 반환
	}

}

