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
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import project.slash.contract.model.EvaluationItem;
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
	public List<MonthlyDataDto> getMonthlyData(LocalDate date) {
		return queryFactory
			.select(Projections.constructor(MonthlyDataDto.class,
				systems.name,
				equipment.name,
				taskRequest.count(),
				systemIncident.incidentTime.sum(),
				Expressions.numberTemplate(Integer.class, "DAY({0})", date),
				systemIncident.count()
			))
			.from(taskRequest)
			.leftJoin(equipment).on(taskRequest.equipment.id.eq(equipment.id))
			.leftJoin(systems).on(equipment.systems.id.eq(systems.id))
			.leftJoin(systemIncident).on(systemIncident.taskRequest.id.eq(taskRequest.id))
			.leftJoin(taskType).on(taskRequest.taskType.id.eq(taskType.id))
			.where(
				taskRequest.createTime.year().eq(date.getYear())
					.and(taskRequest.createTime.month().eq(date.getMonthValue()))
					.and(taskRequest.createTime.dayOfMonth().loe(date.getDayOfMonth()))
			)
			.groupBy(equipment.name)
			.orderBy(systems.name.asc(), equipment.name.asc())
			.fetch();
	}

	@Override
	public ResponseServiceTaskDto getServiceTaskStatics(Long evaluationItemId, LocalDateTime startDate,
		LocalDateTime endDate) {

		// evaluationItem 개별 조회 (항상 존재해야 함)
		EvaluationItem evaluationItemData = queryFactory
			.selectFrom(evaluationItem)
			.where(evaluationItem.id.eq(evaluationItemId))
			.fetchOne();

		// totalWeight 서브쿼리
		Integer totalWeight = queryFactory
			.select(evaluationItem.weight.sum())
			.from(evaluationItem)
			.where(evaluationItem.contract.id.eq(
				queryFactory.select(evaluationItem.contract.id)
					.from(evaluationItem)
					.where(evaluationItem.id.eq(evaluationItemId))
			))
			.fetchOne();

		// 요청 완료 건수 서브쿼리
		Long dueOnTimeCount = queryFactory
			.select(taskRequest.status.count())
			.from(taskRequest)
			.where(taskRequest.dueOnTime.eq(true))
			.where(taskRequest.createTime.between(startDate, endDate))
			.where(taskRequest.taskType.evaluationItem.id.eq(evaluationItemId))
			.fetchOne();

		// 전체 요청 건수 조회
		Long taskRequestCount = queryFactory
			.select(taskRequest.count())
			.from(taskRequest)
			.where(taskRequest.createTime.between(startDate, endDate))
			.where(taskRequest.taskType.evaluationItem.id.eq(evaluationItemId))
			.fetchOne();

		// 기본값 설정 (데이터가 없는 경우)
		return new ResponseServiceTaskDto(
			evaluationItemData,                       // 항상 조회된 evaluationItem
			taskRequestCount == null ? 0 : taskRequestCount, // 요청이 없으면 0
			totalWeight == null ? 0 : totalWeight,    // totalWeight가 없으면 0
			dueOnTimeCount == null ? 100 : dueOnTimeCount                          // 점수 계산 (기본 100)
		);
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
			ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));               // 두 번째 필드 (calculateTime)
			ps.setString(3, dto.getServiceType());                                    // 세 번째 필드 (service_type)
			ps.setString(4, dto.getGrade());                                          // 네 번째 필드 (grade)
			ps.setDouble(5, dto.getScore());                                          // 다섯 번째 필드 (score)
			ps.setString(6, dto.getPeriod());                                         // 여섯 번째 필드 (period)
			ps.setDouble(7, dto.getWeightedScore());                                  // 일곱 번째 필드 (weighted_score)
			ps.setBoolean(8, dto.isApprovalStatus());                                 // 여덟 번째 필드 (approval_status)
			ps.setLong(9, dto.getTotalDowntime());                                    // 아홉 번째 필드 (total_downtime)
			ps.setLong(10, dto.getRequestCount());                                    // 열 번째 필드 (request_count)
			ps.setLong(11, dto.getEvaluationItemId());                                // 열한 번째 필드 (evaluation_item_id)
			ps.setString(12, dto.getTargetSystem());                                  // 열두 번째 필드 (target_system)
			ps.setDouble(13, dto.getEstimate());                                      // 열세 번째 필드 (estimate)
			ps.setLong(14, dto.getSystemIncidentCount());                             // 열네 번째 필드 (system_incident_count)
			ps.setLong(15, dto.getDueOnTimeCount());                                  // 열다섯 번째 필드 (due_on_time_count)
			ps.setString(16, dto.getTargetEquipment());                               // 열여섯 번째 필드 (target_equipment)
			ps.setBoolean(17, dto.isAuto());                                       // 열일곱 번째 필드 (is_auto)
		});
	}

}
