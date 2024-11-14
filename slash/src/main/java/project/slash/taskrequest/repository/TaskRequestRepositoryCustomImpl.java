package project.slash.taskrequest.repository;

import static project.slash.contract.model.QContract.*;
import static project.slash.contract.model.QEvaluationItem.*;
import static project.slash.system.model.QEquipment.*;
import static project.slash.system.model.QSystems.*;
import static project.slash.taskrequest.model.QTaskRequest.*;
import static project.slash.taskrequest.model.QTaskType.*;
import static project.slash.taskrequest.model.constant.RequestStatus.*;
import static project.slash.user.model.QUser.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.slash.statistics.dto.IncidentInfoDto;
import project.slash.system.model.QSystems;
import project.slash.taskrequest.dto.request.RequestManagementDto;
import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.SystemCountDto;
import project.slash.taskrequest.dto.response.TaskRequestOfManagerDto;
import project.slash.taskrequest.dto.response.TaskTypeCountDto;

import project.slash.taskrequest.model.QTaskRequest;
import project.slash.taskrequest.model.QTaskType;
import project.slash.taskrequest.model.constant.RequestStatus;

@Repository
@RequiredArgsConstructor
public class TaskRequestRepositoryCustomImpl implements TaskRequestRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	// 처리 상태 별 카운트 수
	@Override
	public List<StatusCountDto> findCountByStatus(int year, int month, String user,Long contractId) {

		return queryFactory
			.select(Projections.constructor(StatusCountDto.class,
				taskRequest.status,
				taskRequest.count()
			))
			.from(taskRequest)
			.leftJoin(taskType)
			.on(taskType.id.eq(taskRequest.taskType.id))
			.leftJoin(evaluationItem)
			.on(evaluationItem.id.eq(taskType.evaluationItem.id))
			.leftJoin(contract)
			.on(contract.id.eq(evaluationItem.contract.id))
			.where(taskRequest.createTime.year().eq(year)
				.and(taskRequest.createTime.month().eq(month))
				.and(taskRequest.manager.id.eq(user))
				.and(contract.id.eq(contractId)))
			.groupBy(taskRequest.status)
			.fetch();

	}

	@Override
	public List<TaskTypeCountDto> findCountByTaskType(int year, int month, String user,Long contractId) {
		return queryFactory
			.select(Projections.constructor(TaskTypeCountDto.class,
				taskType.type,
				taskRequest.count()
			))
			.from(taskType)
			.leftJoin(taskRequest)
			.on(taskType.id.eq(taskRequest.taskType.id))
			.leftJoin(evaluationItem)
			.on(evaluationItem.id.eq(taskType.evaluationItem.id))
			.leftJoin(contract)
			.on(contract.id.eq(evaluationItem.contract.id))
			.where(taskRequest.createTime.year().eq(year)
				.and(taskRequest.createTime.month().eq(month))
				.and(taskRequest.manager.id.eq(user))
				.and(contract.id.eq(contractId)))
			.groupBy(taskType.type)
			.fetch();

	}

	@Override
	public List<SystemCountDto> findCountBySystem(int year, int month, String user, Long contractId) {
		return queryFactory
			.select(Projections.constructor(SystemCountDto.class,
				systems.name, taskRequest.count()))
			.from(taskRequest)
			.leftJoin(equipment)
			.on(equipment.id.eq(taskRequest.equipment.id))
			.leftJoin(systems)
			.on(systems.id.eq(equipment.systems.id))
			.leftJoin(taskType)
			.on(taskType.id.eq(taskRequest.taskType.id))
			.leftJoin(evaluationItem)
			.on(evaluationItem.id.eq(taskType.evaluationItem.id))
			.leftJoin(contract)
			.on(contract.id.eq(evaluationItem.contract.id))
			.where(taskRequest.createTime.year().eq(year)
				.and(taskRequest.createTime.month().eq(month))
				.and(taskRequest.manager.id.eq(user))
				.and(contract.id.eq(contractId)))
			.groupBy(systems.id)
			.fetch();

	}

	@Override
	public List<TaskRequestOfManagerDto> findTaskRequestOfManager() {
		return queryFactory
			.select(Projections.constructor(TaskRequestOfManagerDto.class, taskRequest.manager.id, user.name,
				taskRequest.count().as("total_count"), Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} THEN 1 ELSE 0 END)",
					taskRequest.status.eq(IN_PROGRESS)).as("in_progress_count")))
			.from(user)
			.leftJoin(taskRequest)
			.on(taskRequest.manager.id.eq(user.id))
			.where(user.role.eq("ROLE_REQUEST_MANAGER"))
			.groupBy(taskRequest.manager.id)
			.orderBy(user.name.asc())
			.fetch();
	}

	@Override
	public Page<RequestManagementDto> findFilteredRequests(String equipmentName, String type,
		String taskDetail, RequestStatus status, String keyword, Pageable pageable) {

		QTaskRequest taskRequestEntity = QTaskRequest.taskRequest;
		QTaskType taskTypeEntity = taskType;
		QSystems systemsEntity = systems;

		BooleanBuilder builder = new BooleanBuilder();

		// 동적 필터 추가
		if (equipmentName != null) {
			builder.and((systemsEntity.name.eq(equipmentName)));
		}

		if (type != null) {
			builder.and(taskRequestEntity.taskType.type.eq(type));
		}
		if (taskDetail != null) {
			builder.and(taskRequestEntity.taskType.taskDetail.eq(taskDetail));
		}

		if (status != null) {
			builder.and(taskRequestEntity.status.eq(status));
		}

		//검색어 필터 추가 (title이나 content에 검색어가 포함된 경우)
		if (keyword != null && !keyword.isEmpty()) {
			builder.and(
				taskRequestEntity.title.containsIgnoreCase(keyword)
					.or(taskRequestEntity.content.containsIgnoreCase(keyword))
			);
		}

		// QueryResults를 통해 결과와 총 개수를 한 번에 조회
		QueryResults<RequestManagementDto> results = queryFactory
			.select(Projections.constructor(RequestManagementDto.class,
				taskRequestEntity.title,
				taskRequestEntity.content,
				taskRequestEntity.dueOnTime,
				taskRequestEntity.status,
				taskRequestEntity.equipment.name,
				taskRequestEntity.taskType.type,
				taskRequestEntity.taskType.taskDetail,
				taskRequestEntity.id,
				taskRequestEntity.requester.name,
				taskRequestEntity.manager.name,
				taskRequestEntity.createTime,
				taskRequestEntity.updateTime
			))
			.from(taskRequestEntity)
			.join(taskRequestEntity.equipment, equipment)
			.join(taskRequestEntity.taskType, taskTypeEntity)
			.leftJoin(systems)
			.on(equipment.systems.name.eq(systemsEntity.name))
			.where(builder)
			.orderBy(taskRequestEntity.createTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		// Page로 반환
		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}

	@Override
	public void updateManagerByRequestId(Long requestId, String managerId) {
		queryFactory
			.update(taskRequest)
			.set(taskRequest.manager.id, managerId)
			.set(taskRequest.status, IN_PROGRESS)
			.set(taskRequest.updateTime, LocalDateTime.now())
			.where(taskRequest.id.eq(requestId))
			.execute();
	}

	@Override
	public void updateDueOnTime(Long requestId, String managerId, RequestStatus status) {
		LocalDateTime currentTime = LocalDateTime.now();

		Integer deadline = queryFactory
			.select(taskType.deadline)
			.from(taskType)
			.where(taskType.id.eq(
				JPAExpressions.select(taskRequest.taskType.id)
					.from(taskRequest)
					.where(taskRequest.id.eq(requestId))))
			.fetchOne();

		queryFactory
			.update(taskRequest)
			.set(taskRequest.dueOnTime,
				Expressions.cases()
					.when(Expressions.dateTimeTemplate(LocalDateTime.class,
							"TIMESTAMPADD(HOUR, {0}, {1})",
							Expressions.constant(deadline),
							taskRequest.createTime)
						.gt(currentTime))
					.then(true)
					.otherwise(false))
			.set(taskRequest.updateTime, currentTime)
			.set(taskRequest.status, status)
			.where(taskRequest.id.eq(requestId)
				.and(taskRequest.manager.id.eq(managerId)))
			.execute();
	}

	@Override
	public IncidentInfoDto getIncidentCount(Long evaluationItemId, LocalDate endDate) {

		LocalDate startDate = endDate.withDayOfMonth(1);

		List<Tuple> result = queryFactory
			.select(taskRequest.dueOnTime, taskRequest.count())
			.from(taskRequest)
			.leftJoin(taskType)
			.on(taskType.id.eq(taskRequest.taskType.id))
			.where(taskType.evaluationItem.id.eq(evaluationItemId)
				.and(taskType.type.eq("장애 요청"))
				.and(taskRequest.createTime.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
			)
			.groupBy(taskRequest.dueOnTime)
			.fetch();

		long totalCount = 0;
		long offTimeCount = 0;

		for (Tuple tuple : result) {
			Boolean dueOnTime = tuple.get(taskRequest.dueOnTime);
			long count = Optional.ofNullable(tuple.get(taskRequest.count())).orElse(0L);

			totalCount += count;
			if (Boolean.FALSE.equals(dueOnTime)) {
				offTimeCount = count;
			}
		}

		return new IncidentInfoDto(totalCount, offTimeCount);

	}

	@Override
	public Long getDuration(Long requestId) {
		return queryFactory
			.select(Expressions.numberTemplate(Long.class,
				"TIMESTAMPDIFF(MINUTE, {0}, {1})",
				taskRequest.createTime, taskRequest.updateTime))
			.from(taskRequest)
			.where(taskRequest.id.eq(requestId))
			.fetchOne();
	}

	public List<StatusCountDto> findStatusCountByUser(int year, int month, String user,Long contractId) {

		return queryFactory
			.select(Projections.constructor(StatusCountDto.class,
				taskRequest.status,
				taskRequest.count()
			))
			.from(taskRequest)
			.leftJoin(taskType)
			.on(taskType.id.eq(taskRequest.taskType.id))
			.leftJoin(evaluationItem)
			.on(evaluationItem.id.eq(taskType.evaluationItem.id))
			.leftJoin(contract)
			.on(contract.id.eq(evaluationItem.contract.id))
			.where(taskRequest.createTime.year().eq(year)
				.and(taskRequest.createTime.month().eq(month))
				.and(taskRequest.requester.id.eq(user))
				.and(contract.id.eq(contractId)))
			.groupBy(taskRequest.status)
			.fetch();

	}


}
