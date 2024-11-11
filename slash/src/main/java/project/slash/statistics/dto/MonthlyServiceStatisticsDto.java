package project.slash.statistics.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MonthlyServiceStatisticsDto {
	@DateTimeFormat(pattern = "yyyy-MM")
	private LocalDate date;
	private LocalDateTime calculateTime;
	private String serviceType;
	private String targetEquipment;
	private String grade;
	private double score;
	private String period;
	private double weightedScore;
	private boolean approvalStatus;
	private long totalDowntime;
	private long requestCount;
	private long evaluationItemId;
	private String targetSystem;
	private double estimate;
	private long systemIncidentCount;
	private long dueOnTimeCount;
}
