package project.slash.statistics.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IndicatorDto {
	private Long evaluationItemId;
	private String category;
	private Boolean isAuto;
	private LocalDate date;	//측정월
	private String grade;
	private double score;
	private double weightedScore;
}
