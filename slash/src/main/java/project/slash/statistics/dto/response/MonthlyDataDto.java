package project.slash.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyDataDto {
	private String systemName;
	private String equipmentName;
	private long requestCount;
	private long totalDownTime;
	private int selectDay;
	private long systemIncidentCount;
}
