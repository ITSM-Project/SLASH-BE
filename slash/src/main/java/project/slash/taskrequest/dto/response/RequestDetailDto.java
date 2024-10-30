package project.slash.taskrequest.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.taskrequest.model.TaskRequest;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class RequestDetailDto {
	private Long requestId;
	private String taskType;
	private String status;
	private boolean dueOnTime;
	private String equipmentName;
	private String taskDetail;
	private String title;
	private String content;
	private String requester;
	private String manager;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime requestTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	public static RequestDetailDto from(TaskRequest taskRequest) {
		return RequestDetailDto.builder()
			.requestId(taskRequest.getId())
			.taskType(taskRequest.getTaskType().getType())
			.status(taskRequest.getStatus().getStatus())
			.dueOnTime(taskRequest.isDueOnTime())
			.equipmentName(taskRequest.getEquipment().getName())
			.taskDetail(taskRequest.getTaskType().getTaskDetail())
			.title(taskRequest.getTitle())
			.content(taskRequest.getContent())
			.requester(taskRequest.getRequester().getName())
			.manager(taskRequest.getManager() != null ? taskRequest.getManager().getName() : "미할당")
			.requestTime(taskRequest.getCreateTime())
			.endTime(taskRequest.getUpdateTime())
			.build();
	}
}
