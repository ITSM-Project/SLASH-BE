package project.slash.taskrequest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateTaskRequestManagerDto {
	private long requestId;
	private String managerId;
}
