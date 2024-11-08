package project.slash.summary.repository;

import static project.slash.system.model.QEquipment.*;
import static project.slash.system.model.QSystems.*;
import static project.slash.systemincident.model.QSystemIncident.*;
import static project.slash.taskrequest.model.QTaskRequest.*;
import static project.slash.taskrequest.model.QTaskType.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import project.slash.summary.dto.evaluation.item.IncidentResolvedRateDto;
import project.slash.summary.dto.evaluation.item.ServiceResolvedRateDto;
import project.slash.summary.dto.evaluation.item.ServiceRuntimeRateDto;
import project.slash.taskrequest.model.constant.RequestStatus;

public class SummaryRepositoryCustomImpl implements SummaryRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public SummaryRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public ServiceRuntimeRateDto getServiceRuntimeRate(Long evaluationItemId, String targetSystem,
		String targetEquipment, String lastDate) {
		return null;
	}

	@Override
	public IncidentResolvedRateDto getIncidentResolvedRate(Long evaluationItemId, String targetSystem,
		String targetEquipment, String lastDate) {
		return queryFactory
			.select(Projections.constructor(IncidentResolvedRateDto.class,
				taskRequest.count(),
				Expressions.asNumber(systemIncident.incidentTime.sum().coalesce(0L)).castToNum(Long.class),
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
				taskRequest.createTime.between(getFirstDayOfMonth(lastDate), getDayOfMonth(lastDate))
					.and(taskType.type.eq("장애 요청"))
					.and(taskType.inclusionStatus.eq(true))
					.and(taskRequest.status.eq(RequestStatus.COMPLETED))
					.and(taskRequest.dueOnTime.eq(true))
					.and(equipment.name.eq(targetEquipment))
					.and(systems.name.eq(targetSystem))
			)
			.fetchOne();

	}

	@Override
	public ServiceResolvedRateDto getServiceResolvedRate(Long evaluationItemId, String targetSystem,
		String targetEquipment, String lastDate) {
		return null;
	}

	// yyyy-MM-dd 형식의 문자열을 LocalDateTime으로 변환
	private LocalDateTime parseToLocalDateTime(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return localDate.atStartOfDay(); // 00:00:00 시간 설정
	}

	// 해당 월의 첫째 날의 LocalDateTime 반환
	public LocalDateTime getFirstDayOfMonth(String date) {
		LocalDateTime localDateTime = parseToLocalDateTime(date);
		return localDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
	}

	// 해당 월의 마지막 날의 LocalDateTime 반환
	public LocalDateTime getDayOfMonth(String date) {
		LocalDateTime localDateTime = parseToLocalDateTime(date);
		return localDateTime.withHour(23).withMinute(59).withSecond(59);
	}
}
