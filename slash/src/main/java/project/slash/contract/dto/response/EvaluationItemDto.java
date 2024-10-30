package project.slash.contract.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.model.ServiceTarget;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluationItemDto {
	private Long categoryId;
	private String categoryName;
	private List<GradeDto> serviceTargets;

	// public static EvaluationItemDto of(Long categoryId, String categoryName, List<ServiceTarget> serviceTargets) {
	// 	return new EvaluationItemDto(categoryId, categoryName, GradeDto.fromServiceTargets(serviceTargets));
	// }
}
