package project.slash.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EvaluatedDto {
	String grade;
	double weightedScore;
	long EvaluationItemId;
	double score;
}
