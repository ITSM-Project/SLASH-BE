package project.slash.taskrequest.mapper;

import org.springframework.stereotype.Component;

import project.slash.system.model.Equipment;
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.response.RequestDetailDto;
import project.slash.taskrequest.model.TaskRequest;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.model.constant.RequestStatus;
import project.slash.user.model.User;

@Component
public class TaskRequestMapper {
	public TaskRequest toEntity(TaskRequestDto taskRequestDto, TaskType taskType, User requester, User mockUser,
		Equipment equipment) {
		return TaskRequest.builder()
			.title(taskRequestDto.getTitle())
			.content(taskRequestDto.getContent())
			.status(RequestStatus.REGISTERED)
			.taskType(taskType)
			.requester(requester)
			.manager(mockUser)
			.equipment(equipment)
			.build();
	}

	public RequestDetailDto toRequestDetailDto(TaskRequest taskRequest) {
		return RequestDetailDto.builder()
			.requestId(taskRequest.getId())
			.taskType(taskRequest.getTaskType().getType())
			.status(taskRequest.getStatus().getStatus())
			.dueOnTime(taskRequest.isDueOnTime())
			.system(taskRequest.getEquipment().getSystems().getName())
			.equipmentName(taskRequest.getEquipment().getName())
			.taskDetail(taskRequest.getTaskType().getTaskDetail())
			.title(taskRequest.getTitle())
			.content(taskRequest.getContent())
			.requester(taskRequest.getRequester().getName())
			.manager(taskRequest.getManager() != null ? taskRequest.getManager().getName() : "미할당")
			.managerId(taskRequest.getManager().getId())
			.requestTime(taskRequest.getCreateTime())
			.endTime(taskRequest.getUpdateTime())
			.build();
	}
}
