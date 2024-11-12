package project.slash.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IndicatorExtraInfoDto {
	private String grade;
	private long requestCount;
	private long incidentTime;

	public static IndicatorExtraInfoDto of(String grade, long requestCount, long incidentTime) {
		return new IndicatorExtraInfoDto(grade, requestCount, incidentTime);
	}
}
