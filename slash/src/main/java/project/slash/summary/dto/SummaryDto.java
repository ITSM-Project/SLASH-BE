package project.slash.summary.dto;

import java.sql.Date;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryDto {

	private Long statisticsId;
	private Date date;
	private String grade;
	private double score;
	private String period;
	private Long evaluationItemId;
	private double weightedScore;
	private boolean approvalStatus;
	private double estimate;
	private Long totalDowntime;
	private Long requestCount;
	private Long dueOnTimeCount;
	private String targetSystem;
	private String serviceType;
	private String targetEquipment;
	private boolean isAuto;
}

