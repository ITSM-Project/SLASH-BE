package project.slash.taskrequest.dto.request;

import lombok.Getter;

@Getter
public class TaskRequestDto {
	private String taskType;
	private Long equipmentId;
	private String taskDetail;
	private boolean serviceRelevance;
	private String title;
	private String content;
}
