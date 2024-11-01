package project.slash.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContractDataDto {
	private String grade;
	private double max;
	private boolean maxInclusive;
	private double min;
	private boolean minInclusive;
	private double score;
	private int weight;
	private int weightTotal;

	@Override
	public String toString() {
		return "ContractDataDto{" +
			"grade='" + grade + '\'' +
			", max=" + max +
			", maxInclusive=" + maxInclusive +
			", min=" + min +
			", minInclusive=" + minInclusive +
			", score=" + score +
			", weight=" + weight +
			", weightTotal=" + weightTotal +
			'}';
	}
}
