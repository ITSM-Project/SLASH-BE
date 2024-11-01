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
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.slash.statistics.dto.MonthlyDataDto;

@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryCustomImpl implements StatisticsRepositoryCustom {
	@Autowired
	private final JPAQueryFactory queryFactory;

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
				Expressions.numberTemplate(Integer.class, "DAY(LAST_DAY({0}))", taskRequest.createTime))) // LAST_DAY 사용
			.from(taskRequest)
			.leftJoin(equipment).on(taskRequest.equipment.id.eq(equipment.id))
			.leftJoin(systems).on(equipment.systems.id.eq(systems.id))
			.leftJoin(systemIncident).on(systemIncident.taskRequest.id.eq(taskRequest.id))
			.leftJoin(taskType).on(taskRequest.taskType.id.eq(taskType.id))// 현재 년-월보다 이전 것만 필터링
			.where(yearMonth.eq(previousMonth))
			.groupBy(yearMonth, systems.name)
			.orderBy(yearMonth.asc())
			.fetch();
	}
}
