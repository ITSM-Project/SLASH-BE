package project.slash.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EvaluatedDto {
	private String grade;
	private double weightedScore;
	private long EvaluationItemId;
	private double score;
}
