package project.slash.taskrequest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {
	private String taskType;
	private String equipmentName;
	private String taskDetail;
	private boolean serviceRelevance;
	private String title;
	private String content;
}
