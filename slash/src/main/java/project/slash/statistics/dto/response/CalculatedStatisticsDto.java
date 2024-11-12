package project.slash.statistics.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime calculatedDate;
	private Boolean isApprove;
}
