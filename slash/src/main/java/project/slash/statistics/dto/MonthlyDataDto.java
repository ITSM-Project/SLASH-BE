package project.slash.statistics.dto;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyDataDto {
	@DateTimeFormat(pattern = "yyyy-MM")
	private String yearMonth;
	private String systemName;
	private long requestCount;
	private long totalDownTime;
	private int lastDay;

	@Override
	public String toString() {
		return "MonthlyDataDto{" +
			"yearMonth=" + yearMonth +
			", systemName='" + systemName + '\'' +
			", requestCount=" + requestCount +
			", totalDownTime=" + totalDownTime +
			", lastDay=" + lastDay +
			'}';
	}
}
