package project.slash.statistics.repository;

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

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatsDto;

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
			.groupBy(systems.name)
			.orderBy(systems.name.asc())
			.fetch();
	}

	@Override
	public void saveMonthlyData(List<MonthlyServiceStatsDto> statsDtoList) {
		String sql = "INSERT INTO statistics (`date`, service_type, grade, score, period, weighted_score, " +
			"approval_status, total_downtime, request_count, evaluation_item_id, target_system, estimate,system_incident_count,due_on_time_count) "
			+
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

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
		});
	}

}
