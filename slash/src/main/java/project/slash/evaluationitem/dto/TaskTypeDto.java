package project.slash.evaluationitem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.taskrequest.model.TaskType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskTypeDto {
	@NotBlank(message = "Task 타입은 필수입니다.")
	private String type;

	@NotBlank(message = "Task 상세는 필수입니다.")
	private String taskDetail;

	@NotNull(message = "데드라인은 필수입니다.")
	private int deadline;

	private boolean serviceRelevance;

	private boolean inclusionStatus;

	public static TaskTypeDto from(TaskType taskType) {
		return new TaskTypeDto(taskType.getType(),
			taskType.getTaskDetail(), taskType.getDeadline(), taskType.isServiceRelevance(),
			taskType.isInclusionStatus());
	}
}
