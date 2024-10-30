package project.slash.taskrequest.repository;

import java.util.Collections;
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

	// 처리 상태 별 카운트수
	@Override
	public List<StatusCountDto> findCountByStatus(int year, int month, String user) {
		QTaskRequest taskRequest = QTaskRequest.taskRequest;
		System.out.println(year);
		try {
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

			// 결과가 없을 경우 처리
			if (results.isEmpty()) {
				System.err.println("데이터 없음: " + year + "년 " + month + "월 " + user + "님 에 대한 처리 상태 별 카운트 수가 없습니다.");
			}
			return results;
		} catch (Exception e) {
			System.err.println("오류 발생: " + e.getMessage());
			return Collections.emptyList(); // 오류가 발생한 경우 빈 리스트 반환
		}
	}

	@Override
	public List<TaskTypeCountDto> findCountByTaskType(int year, int month, String user) {
		QTaskType taskType = QTaskType.taskType;
		QTaskRequest taskRequest = QTaskRequest.taskRequest;

		try {
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

			// 결과가 없을 경우 처리
			if (results.isEmpty()) {
				System.err.println("데이터 없음: " + year + "년 " + month + "월 " + user + "님 에 대한 업무 유형별 요청 건수가 없습니다.");
			}
			return results;

		} catch (Exception e) {
			System.err.println("오류 발생: " + e.getMessage());
			return Collections.emptyList(); // 오류가 발생한 경우 빈 리스트 반환
		}
	}

	@Override
	public List<SystemCountDto> findCountBySystem(int year, int month, String user) {
		QSystems systems = QSystems.systems;
		QEquipment equipment = QEquipment.equipment;
		QTaskRequest taskRequest = QTaskRequest.taskRequest;

		try {
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

			// 결과가 없을 경우 처리
			if (results.isEmpty()) {
				System.err.println("데이터 없음: " + year + "년 " + month + "월 " + user + "님 에 대한 장비 유형별 요청 건수가 없습니다.");
			}
			return results;

		} catch (Exception e) {
			System.err.println("오류 발생: " + e.getMessage());
			return Collections.emptyList(); // 오류가 발생한 경우 빈 리스트 반환
		}
	}

}
