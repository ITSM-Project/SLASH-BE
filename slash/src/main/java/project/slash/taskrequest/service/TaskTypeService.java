package project.slash.taskrequest.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.slash.taskrequest.dto.response.AllTaskTypeDto;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
public class TaskTypeService {
	private final TaskTypeRepository taskTypeRepository;

	public List<AllTaskTypeDto> allTaskTypes(Long contractId) {
		return taskTypeRepository.findAllTaskTypes(contractId);
	}

	public List<String> getDistinctTaskTypes() {
		return Stream.concat(
			Stream.of("전체"),
			taskTypeRepository.findDistinctByTypeNotNull().stream()
				.map(TaskType::getType)
				.distinct()
		).toList();
	}

	public List<String> getDistinctTaskDetails() {
		return Stream.concat(
			Stream.of("전체"),
			taskTypeRepository.findDistinctByTaskDetailNotNull().stream()
				.map(TaskType::getTaskDetail)
				.distinct()
		).toList();
	}
}

