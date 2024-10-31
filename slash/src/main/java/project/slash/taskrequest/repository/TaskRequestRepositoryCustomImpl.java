package project.slash.taskrequest.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.slash.system.model.QEquipment;
import project.slash.system.model.QSystems;
import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.SystemCountDto;
import project.slash.taskrequest.dto.response.TaskTypeCountDto;
import project.slash.taskrequest.model.QTaskRequest;
import project.slash.taskrequest.model.QTaskType;

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
				.and(taskRequest.requester.id.eq(user)))
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
				.and(taskRequest.requester.id.eq(user)))
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
				.and(taskRequest.requester.id.eq(user)))
			.groupBy(systems.id)
			.fetch();

		return results;

	}

}
