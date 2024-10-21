package project.slash.taskrequest.repository;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.slash.taskrequest.dto.request.CreateTaskTypeDto;
import project.slash.taskrequest.model.TaskType;

@SpringBootTest
@ActiveProfiles("test")
class TaskTypeRepositoryTest {
	@Autowired
	private TaskTypeRepository taskTypeRepository;

	@BeforeEach
	void initData() {
		CreateTaskTypeDto serviceType = new CreateTaskTypeDto("서비스 요청", "업무 지원", 0, false, false);
		CreateTaskTypeDto incidentType = new CreateTaskTypeDto("장애 요청", "단순 장애", 4, true, true);
		TaskType taskType1 = TaskType.from(serviceType);
		TaskType taskType2 = TaskType.from(incidentType);

		taskTypeRepository.saveAll(List.of(taskType1, taskType2));
	}

	@AfterEach
	void tearDown() {
		taskTypeRepository.deleteAllInBatch();
	}

	@DisplayName("업무 유형이 null인경우 모든 유형을 조회할 수 있다.")
	@Test
	void findAllByTaskType() {
		//given
		String taskType = null;

		//when
		List<String> result = taskTypeRepository.findAllByTaskType(taskType);

		//then
		Assertions.assertThat(result).hasSize(2)
			.containsExactlyInAnyOrder("업무 지원", "단순 장애");
	}

	@DisplayName("업무 유형이 서비스 요청인 경우 서비스 요청 업무 유형을 조회할 수 있다.")
	@Test
	void findAllByTaskTypeWithServiceType() {
		//given
		String taskType = "서비스 요청";

		//when
		List<String> result = taskTypeRepository.findAllByTaskType(taskType);

		//then
		Assertions.assertThat(result).hasSize(1)
			.containsExactlyInAnyOrder("업무 지원");
	}
}
