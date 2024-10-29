package project.slash.contract.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.contract.dto.GradeDto;

@Getter
@AllArgsConstructor
public class EvaluationItemDto {
	private Long categoryId;

	private String categoryName;

	private ServiceDetailDto serviceDetailDto;

	@Getter
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class ServiceDetailDto {
		private String period;
		private String purpose;
		private String formula;
		private List<GradeDto> serviceTargets;
	}
}
