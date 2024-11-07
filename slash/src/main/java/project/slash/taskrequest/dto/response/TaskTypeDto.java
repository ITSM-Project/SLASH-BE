package project.slash.taskrequest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TaskTypeDto {
	private Long taskTypeId;
	private String taskDetail;
	private boolean serviceRelevance;
}
