package project.slash.taskrequest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.taskrequest.model.constant.RequestStatus;

@Getter
@AllArgsConstructor
public class StatusCountDto {
	private RequestStatus name;
	private long count;
}
