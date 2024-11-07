package project.slash.statistics.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 출력 테스트할때 필요해서 toString()넣었습니다. 개발끝나면 삭제 예정입니다.
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyServiceStatisticsDto {
	@DateTimeFormat(pattern = "yyyy-MM")
	private LocalDate date;
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
	private Boolean isAuto;

	@Override
	public String toString() {
		return "MonthlyServiceStatisticsDto{" +
			"date=" + date +
			", serviceType='" + serviceType + '\'' +
			", targetEquipment='" + targetEquipment + '\'' +
			", grade='" + grade + '\'' +
			", score=" + score +
			", period='" + period + '\'' +
			", weightedScore=" + weightedScore +
			", approvalStatus=" + approvalStatus +
			", totalDowntime=" + totalDowntime +
			", requestCount=" + requestCount +
			", evaluationItemId=" + evaluationItemId +
			", targetSystem='" + targetSystem + '\'' +
			", estimate=" + estimate +
			", systemIncidentCount=" + systemIncidentCount +
			", dueOnTimeCount=" + dueOnTimeCount +
			", isAuto=" + isAuto +
			'}';
	}
}
