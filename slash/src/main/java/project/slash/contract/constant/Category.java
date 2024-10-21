package project.slash.contract.constant;

import static project.slash.contract.exception.ContractErrorCode.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.common.exception.BusinessException;

@AllArgsConstructor
@Getter
public enum Category {
	SERVICE_UPTIME_RATE("서비스 가동률"),
	INCIDENT_RATE("장애 적기처리율"),
	SERVICE_REQUEST_RATE("서비스 요청 적기처리율");

	private final String category;

	public static Category getCategory(String category) {
		for (Category c : Category.values()) {
			if (c.getCategory().equals(category)) {
				return c;
			}
		}

		throw new BusinessException(NOT_FOUND_ITEMS);
	}
}
