package project.slash.taskrequest.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.taskrequest.dto.request.CreateTaskTypeDto;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskRequestService {
	private final TaskTypeRepository taskTypeRepository;

	@Transactional
	public void createTaskType(List<CreateTaskTypeDto> createTaskTypes) {
		List<TaskType> taskTypes = createTaskTypes.stream()
			.map(TaskType::from)
			.toList();

		taskTypeRepository.saveAll(taskTypes);
	}

	public List<String> showTaskTypes(String taskType) {
		return taskTypeRepository.findAllByTaskType(taskType);
	}
}
