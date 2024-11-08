package project.slash.statistics.dto.response;

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
