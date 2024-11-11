package project.slash.statistics.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.statistics.model.Statistics;

@AllArgsConstructor
@Getter
public class IndicatorDto {
	private Long evaluationItemId;
	private String category;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date;
	private boolean isAuto;
	private String grade;
	private double score;
	private String targetSystem;

	public static IndicatorDto of(Statistics statistics) {
		return new IndicatorDto(
			statistics.getEvaluationItem().getId(),
			statistics.getEvaluationItem().getCategory(),
			statistics.getDate(),
			statistics.isAuto(),
			statistics.getGrade(),
			statistics.getScore(),
			statistics.getTargetSystem()
		);
	}
}
