package project.slash.contract.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.GradeDto;
import project.slash.evaluationitem.dto.TaskTypeDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluationItemDetailDto{
	private Long categoryId;
	private String categoryName;
	private Integer weight;
	private String period;
	private String purpose;
	private String formula;
	private String unit;
	private List<GradeDto> serviceTargets;
	private List<TaskTypeDto> taskTypes;

	public EvaluationItemDetailDto withTaskTypes(List<TaskTypeDto> taskTypes) {
		return new EvaluationItemDetailDto(
			this.categoryId,
			this.categoryName,
			this.weight,
			this.period,
			this.purpose,
			this.formula,
			this.unit,
			this.serviceTargets,
			taskTypes
		);
	}
}
