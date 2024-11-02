package project.slash.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyDataDto {
	private String yearMonth;
	private String systemName;
	private long requestCount;
	private long totalDownTime;
	private int lastDay;
	private long systemIncidentCount;
}
