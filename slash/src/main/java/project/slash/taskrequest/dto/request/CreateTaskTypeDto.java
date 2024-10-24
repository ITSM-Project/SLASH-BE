package project.slash.taskrequest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskTypeDto {
	private String taskType;    //장애 요청, 서비스 요청
	private String taskDetail;
	private int deadline;
	private boolean serviceRelevance;
	private boolean inclusionStatus;
}
