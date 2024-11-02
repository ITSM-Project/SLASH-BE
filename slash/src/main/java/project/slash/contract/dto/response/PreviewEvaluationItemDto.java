package project.slash.contract.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.contract.dto.GradeDto;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PreviewEvaluationItemDto {
	private Long evaluationItemId;
	private String category;
	private List<GradeDto> serviceTargets;
}
