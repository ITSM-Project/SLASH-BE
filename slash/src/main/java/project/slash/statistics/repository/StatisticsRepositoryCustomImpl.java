package project.slash.statistics.repository;

import static project.slash.statistics.model.QStatistics.*;
import static project.slash.system.model.QEquipment.*;
import static project.slash.system.model.QSystems.*;
import static project.slash.systemincident.model.QSystemIncident.*;
import static project.slash.taskrequest.model.QTaskRequest.*;
import static project.slash.taskrequest.model.QTaskType.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyIncidentDataDto;
import project.slash.statistics.dto.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.taskrequest.controller.TaskRequestController;
import project.slash.taskrequest.model.constant.RequestStatus;

@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryCustomImpl implements StatisticsRepositoryCustom {
	@Autowired
	private final JPAQueryFactory queryFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

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
	public List<MonthlyIncidentDataDto> getMonthlyIncidentData() {
		StringTemplate yearMonth = Expressions.stringTemplate(
			"DATE_FORMAT({0}, '%Y-%m')",
			taskRequest.createTime);

		LocalDate previousMonthDate = LocalDate.now().minusMonths(1);
		String previousMonth = previousMonthDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));

		return queryFactory
			.select(Projections.constructor(MonthlyIncidentDataDto.class,
				yearMonth,
				systems.name,
				equipment.name,
				taskRequest.count(),
				Expressions.asNumber(systemIncident.incidentTime.sum().coalesce(0L)).castToNum(Long.class),
				Expressions.numberTemplate(Integer.class, "DAY(LAST_DAY({0}))", taskRequest.createTime),
				Expressions.asNumber(systemIncident.count().coalesce(0L)).castToNum(Long.class),
				Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} THEN 1 ELSE 0 END)",
					taskRequest.dueOnTime.isTrue()).as("dueOnTimeCount"))
			)
			.from(taskRequest)
			.leftJoin(equipment).on(taskRequest.equipment.id.eq(equipment.id))
			.leftJoin(systems).on(equipment.systems.id.eq(systems.id))
			.leftJoin(systemIncident).on(systemIncident.taskRequest.id.eq(taskRequest.id))
			.leftJoin(taskType).on(taskRequest.taskType.id.eq(taskType.id))
			.where(
				yearMonth.eq(previousMonth)
					.and(taskType.type.eq("장애 요청"))
					.and(taskType.inclusionStatus.eq(true))
					.and(taskRequest.status.eq(RequestStatus.COMPLETED))
					.and(taskRequest.dueOnTime.eq(true))
			)
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
}

