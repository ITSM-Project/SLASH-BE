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
			.map(TaskTypeDto::from)
			.toList();
	}

	public List<TaskType> toTaskTypeList(List<TaskTypeDto> taskTypeDtos, EvaluationItem evaluationItem) {
		return taskTypeDtos.stream()
			.map(taskTypeDto -> TaskType.from(taskTypeDto, evaluationItem))
			.toList();
	}
}
