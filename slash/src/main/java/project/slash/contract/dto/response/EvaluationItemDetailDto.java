package project.slash.contract.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.TaskTypeDto;

@Getter
@AllArgsConstructor
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

	public void setTaskTypes(List<TaskTypeDto> taskTypes) {
		this.taskTypes = taskTypes;
	}
}
