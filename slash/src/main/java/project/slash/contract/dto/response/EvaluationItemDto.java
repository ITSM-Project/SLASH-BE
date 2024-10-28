package project.slash.contract.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EvaluationItemDto {
	private Long categoryId;

	private String categoryName;

	private ServiceDetailDto serviceDetailDto;
}
