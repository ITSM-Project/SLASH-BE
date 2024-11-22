package project.slash.statistics.repository;

import static project.slash.contract.model.QEvaluationItem.*;
import static project.slash.system.model.QEquipment.*;
import static project.slash.system.model.QSystems.*;
import static project.slash.systemincident.model.QSystemIncident.*;
import static project.slash.taskrequest.model.QTaskRequest.*;
import static project.slash.taskrequest.model.QTaskType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import project.slash.statistics.dto.response.MonthlyDataDto;
import project.slash.statistics.dto.response.ResponseServiceTaskDto;
import project.slash.statistics.dto.response.ResponseStatisticsDto;

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
	public List<MonthlyDataDto> getMonthlyData(LocalDate date, long contractId) {
		return queryFactory
			.select(Projections.constructor(MonthlyDataDto.class,
				systems.name,
				equipment.name,
				taskRequest.count().coalesce(0L),
				systemIncident.incidentTime.sum().coalesce(0L),
				Expressions.numberTemplate(Integer.class, "DAY({0})", date),
				systemIncident.count().coalesce(0L)
			))
			.from(equipment)
			.leftJoin(systems)
			.on(equipment.systems.id.eq(systems.id))
			.leftJoin(taskRequest)
			.on(taskRequest.equipment.id.eq(equipment.id)
				.and(taskRequest.createTime.year().eq(date.getYear()))
				.and(taskRequest.createTime.month().eq(date.getMonthValue()))
				.and(taskRequest.createTime.dayOfMonth().loe(date.getDayOfMonth())))
			.leftJoin(systemIncident)
			.on(systemIncident.taskRequest.id.eq(taskRequest.id))
			.leftJoin(taskType)
			.on(taskRequest.taskType.id.eq(taskType.id))
			.leftJoin(evaluationItem)
			.on(taskType.evaluationItem.id.eq(evaluationItem.id).and(evaluationItem.contract.id.eq(contractId)))
			.groupBy(systems.name, equipment.name)
			.orderBy(systems.name.asc(), equipment.name.asc())
			.fetch();
	}

	@Override
	public ResponseServiceTaskDto getServiceTaskStatics(Long evaluationItemId, LocalDateTime startDate,
		LocalDateTime endDate) {

		// evaluationItemId의 contractId 가져오기
		Long contractId = new JPAQuery<>(entityManager)
			.select(evaluationItem.contract.id)
			.from(evaluationItem)
			.where(evaluationItem.id.eq(evaluationItemId))
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
				.where(taskRequest.dueOnTime.eq(true))
				.where(taskRequest.createTime.between(startDate, endDate))
				.where(taskRequest.taskType.evaluationItem.id.eq(evaluationItemId))
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
			.where(evaluationItem.id.eq(evaluationItemId))
			.fetchOne(); // ResponseServiceTaskDto 타입으로 반환
	}

	@Override
	public void saveMonthlyData(List<ResponseStatisticsDto> statsDtoList) {
		String sql =
			"INSERT INTO statistics (`date`, calculate_time, service_type, grade, score, period, weighted_score, " +
				"approval_status, total_downtime, request_count, evaluation_item_id, target_system, estimate, system_incident_count, due_on_time_count, target_equipment, is_auto) "
				+
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, statsDtoList, 50, (ps, dto) -> {
			ps.setDate(1, java.sql.Date.valueOf(dto.getDate()));
			ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
			ps.setString(3, dto.getServiceType());
			ps.setString(4, dto.getGrade());
			ps.setDouble(5, dto.getScore());
			ps.setString(6, dto.getPeriod());
			ps.setDouble(7, dto.getWeightedScore());
			ps.setBoolean(8, dto.isApprovalStatus());
			ps.setLong(9, dto.getTotalDowntime());
			ps.setLong(10, dto.getRequestCount());
			ps.setLong(11, dto.getEvaluationItemId());
			ps.setString(12, dto.getTargetSystem());
			ps.setDouble(13, dto.getEstimate());
			ps.setLong(14,
				dto.getSystemIncidentCount());
			ps.setLong(15, dto.getDueOnTimeCount());
			ps.setString(16, dto.getTargetEquipment());
			ps.setBoolean(17, dto.isAuto());
		});
	}

}
