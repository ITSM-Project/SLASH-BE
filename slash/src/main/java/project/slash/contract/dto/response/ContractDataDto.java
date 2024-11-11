package project.slash.contract.dto.response;

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
	private long evaluationItemId;
	private String category;
}
