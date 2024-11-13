package project.slash.statistics.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IndicatorDto {
	private Long evaluationItemId;
	private String category;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date;
	private LocalDate calculateTime;
	private boolean isAuto;
	private String grade;
	private double score;
	private String targetSystem;
}
