package project.slash.taskrequest.repository;

import static project.slash.taskrequest.model.QTaskRequest.*;
import static project.slash.taskrequest.model.constant.RequestStatus.*;
import static project.slash.user.model.QUser.*;

import static project.slash.system.model.QEquipment.*;
import static project.slash.system.model.QSystems.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.slash.system.model.QEquipment;
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
	public List<StatusCountDto> findCountByStatus(int year, int month, String user) {
		QTaskRequest taskRequest = QTaskRequest.taskRequest;

		List<StatusCountDto> results = queryFactory
			.select(Projections.constructor(StatusCountDto.class,
				taskRequest.status,
				taskRequest.count()
			))
			.from(taskRequest)
			.where(taskRequest.createTime.year().eq(year)
				.and(taskRequest.createTime.month().eq(month))
				.and(taskRequest.manager.id.eq(user)))
			.groupBy(taskRequest.status)
			.fetch();

		return results;
	}

	@Override
	public List<TaskTypeCountDto> findCountByTaskType(int year, int month, String user) {
		QTaskType taskType = QTaskType.taskType;
		QTaskRequest taskRequest = QTaskRequest.taskRequest;

		List<TaskTypeCountDto> results = queryFactory
			.select(Projections.constructor(TaskTypeCountDto.class,
				taskType.type,
				taskRequest.count()
			))
			.from(taskType)
			.join(taskRequest).on(taskType.id.eq(taskRequest.taskType.id))
			.where(taskRequest.createTime.year().eq(year)
				.and(taskRequest.createTime.month().eq(month))
				.and(taskRequest.manager.id.eq(user)))
			.groupBy(taskType.type)
			.fetch();

		return results;

	}

	@Override
	public List<SystemCountDto> findCountBySystem(int year, int month, String user) {
		QSystems systems = QSystems.systems;
		QEquipment equipment = QEquipment.equipment;
		QTaskRequest taskRequest = QTaskRequest.taskRequest;

		List<SystemCountDto> results = queryFactory
			.select(Projections.constructor(SystemCountDto.class,
				systems.name, taskRequest.count()))
			.from(systems)
			.join(equipment).on(systems.id.eq(equipment.systems.id))
			.join(taskRequest).on(equipment.id.eq(taskRequest.equipment.id))
			.where(taskRequest.createTime.year().eq(year)
				.and(taskRequest.createTime.month().eq(month))
				.and(taskRequest.manager.id.eq(user)))
			.groupBy(systems.id)
			.fetch();

		return results;

	}

	@Override
	public List<TaskRequestOfManagerDto> findTaskRequestOfManager() {
		return queryFactory
			.select(Projections.constructor(TaskRequestOfManagerDto.class, taskRequest.manager.id, user.name,
				taskRequest.count().as("total_count"), Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} THEN 1 ELSE 0 END)",
					taskRequest.status.eq(IN_PROGRESS)).as("in_progress_count")))
			.from(taskRequest)
			.leftJoin(user)
			.on(taskRequest.manager.id.eq(user.id))
			.groupBy(taskRequest.manager.id)
			.orderBy(user.name.asc())
			.fetch();
	}

	@Override
	public Page<RequestManagementDto> findFilteredRequests(String equipmentName, String type,
		String taskDetail, RequestStatus status, String keyword, Pageable pageable) {

		QTaskRequest taskRequestEntity = QTaskRequest.taskRequest;
		QTaskType taskTypeEntity = QTaskType.taskType;
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

}
