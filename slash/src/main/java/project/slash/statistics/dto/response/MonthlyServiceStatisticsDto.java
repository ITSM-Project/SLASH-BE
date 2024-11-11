package project.slash.statistics.dto.response;

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

	public MonthlyServiceStatisticsDto(LocalDate date, String serviceType, String targetEquipment, String grade,
		double score, String period, double weightedScore, boolean approvalStatus, long totalDowntime,
		long requestCount,
		long evaluationItemId, String targetSystem, double estimate, long systemIncidentCount, long dueOnTimeCount) {
		this.date = date;
		this.serviceType = serviceType;
		this.targetEquipment = targetEquipment;
		this.grade = grade;
		this.score = score;
		this.period = period;
		this.weightedScore = weightedScore;
		this.approvalStatus = approvalStatus;
		this.totalDowntime = totalDowntime;
		this.requestCount = requestCount;
		this.evaluationItemId = evaluationItemId;
		this.targetSystem = targetSystem;
		this.estimate = estimate;
		this.systemIncidentCount = systemIncidentCount;
		this.dueOnTimeCount = dueOnTimeCount;
	}
}
