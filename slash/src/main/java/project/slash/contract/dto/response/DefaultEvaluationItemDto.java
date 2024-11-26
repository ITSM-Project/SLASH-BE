package project.slash.contract.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DefaultEvaluationItemDto {
	private Integer weight;

	private String period;

	private String purpose;

	private String formula;

	private String unit;
}
