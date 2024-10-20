package project.slash.taskrequest.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestStatus {
	REGISTERED("접수 완료"),
	IN_PROGRESS("진행중"),
	COMPLETED("처리 완료");

	private final String status;
}
