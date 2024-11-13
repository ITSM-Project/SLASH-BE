package project.slash.statistics.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalculatedStatisticsDto {
	private Long statisticsId;
	private Long evaluationItemId;
	private String category;
	private Boolean isAuto;
	private LocalDate calculateRange;
	private LocalDate calculatedDate;
	private Boolean isApprove;
}
