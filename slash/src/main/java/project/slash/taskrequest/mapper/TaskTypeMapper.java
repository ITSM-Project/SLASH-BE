package project.slash.taskrequest.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.contract.dto.TaskTypeDto;
import project.slash.contract.model.EvaluationItem;
import project.slash.taskrequest.model.TaskType;

@Component
public class TaskTypeMapper {
	public List<TaskTypeDto> toTaskTypeDtoList(List<TaskType> taskTypes) {
		return taskTypes
			.stream()
			.map(this::toTaskTypeDto)
			.toList();
	}

	public List<TaskType> toTaskTypeList(List<TaskTypeDto> taskTypeDtos, EvaluationItem evaluationItem) {
		return taskTypeDtos.stream()
			.map(taskTypeDto -> toEntity(taskTypeDto, evaluationItem))
			.toList();
	}

	public TaskTypeDto toTaskTypeDto(TaskType taskType) {
		return new TaskTypeDto(taskType.getType(),
			taskType.getTaskDetail(), taskType.getDeadline(), taskType.isServiceRelevance(),
			taskType.isInclusionStatus());
	}

	public TaskType toEntity(TaskTypeDto taskTypeDto, EvaluationItem evaluationItem) {
		return TaskType.builder()
			.type(taskTypeDto.getType())
			.taskDetail(taskTypeDto.getTaskDetail())
			.deadline(taskTypeDto.getDeadline())
			.serviceRelevance(taskTypeDto.isServiceRelevance())
			.inclusionStatus(taskTypeDto.isInclusionStatus())
			.evaluationItem(evaluationItem)
			.build();
	}
}
