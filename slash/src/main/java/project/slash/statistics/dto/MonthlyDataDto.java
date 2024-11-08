package project.slash.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
public class MonthlyDataDto {
	private String systemName;
	private String equipmentName;
	private long requestCount;
	private long totalDownTime;
	private int selectDay;
	private long systemIncidentCount;
}
