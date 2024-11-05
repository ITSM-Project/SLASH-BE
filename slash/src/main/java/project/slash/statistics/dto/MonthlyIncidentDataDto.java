package project.slash.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyIncidentDataDto {

	private String yearMonth;
	private String systemName;
	private String equipmentName;
	private long requestCount;
	private long totalDownTime;
	private int lastDay;
	private long systemIncidentCount;
	private long dueOnTimeCount;

	@Override
	public String toString() {
		return "MonthlyIncidentDataDto{" +
			"yearMonth='" + yearMonth + '\'' +
			", systemName='" + systemName + '\'' +
			", equipmentName='" + equipmentName + '\'' +
			", requestCount=" + requestCount +
			", totalDownTime=" + totalDownTime +
			", lastDay=" + lastDay +
			", systemIncidentCount=" + systemIncidentCount +
			", dueOnTimeCount=" + dueOnTimeCount +
			'}';
	}
}