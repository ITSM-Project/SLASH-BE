package project.slash.taskrequest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskRequestOfManagerDto {
	private String managerId;
	private String managerName;
	private long totalCount;
	private long inProgressCount;
}
