package project.slash.taskrequest.repository;

import static project.slash.system.model.QEquipment.*;
import static project.slash.system.model.QSystems.*;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import project.slash.system.model.QSystems;
import project.slash.taskrequest.dto.request.TaskResponseRequestDTO;
import project.slash.taskrequest.model.QTaskRequest;
import project.slash.taskrequest.model.QTaskType;
import project.slash.taskrequest.model.constant.RequestStatus;

@Repository
public class TaskRequestRepositoryCustomImpl implements TaskRequestRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public TaskRequestRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<TaskResponseRequestDTO> findFilteredRequests(String equipmentName, String type, String taskDetail,
		RequestStatus status, Pageable pageable) {
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

		// QueryResults를 통해 결과와 총 개수를 한 번에 조회
		QueryResults<TaskResponseRequestDTO> results = queryFactory
			.select(Projections.constructor(TaskResponseRequestDTO.class,
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
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		// Page로 반환
		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}
}
