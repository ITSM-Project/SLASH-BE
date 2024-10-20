package project.slash.serviceassessment.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
	SERVICE_UPTIME_RATE("서비스 가동률"),
	INCIDENT_RATE("장애 적기처리율"),
	SERVICE_REQUEST_RATE("서비스 요청 적기처리율");

	private final String category;
}
