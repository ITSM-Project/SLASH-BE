package project.slash.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WeightedScore {
	private String category;
	private double weightedScore;
}
