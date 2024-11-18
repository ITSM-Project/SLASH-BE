package project.slash.contract.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationItemCategoryDto {
	private Long evaluationItemId;
	private String category;
}
