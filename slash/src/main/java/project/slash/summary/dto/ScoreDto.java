package project.slash.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScoreDto {
	private double score;
	private String grade;
	private double weightedScore;
	private long evaluationItemId;

}
