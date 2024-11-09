package project.slash.contract.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.TaskTypeDto;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluationItemDetailDto {
	private Long evaluationItemId;

	private String category;    //서비스 항목

	private Integer weight;

	private String period;

	private String purpose;

	private Boolean isAuto;

	private String formula;

	private String unit;

	private List<GradeDto> serviceTargets;

	private List<TaskTypeDto> taskTypes;

	public static EvaluationItemDetailDto createAll(EvaluationItemDto evaluationItemDto, List<TaskTypeDto> taskTypeDto) {
		return EvaluationItemDetailDto.builder()
			.evaluationItemId(evaluationItemDto.getEvaluationItemId())
			.category(evaluationItemDto.getCategory())
			.weight(evaluationItemDto.getWeight())
			.period(evaluationItemDto.getPeriod())
			.purpose(evaluationItemDto.getPurpose())
			.isAuto(true)
			.formula(evaluationItemDto.getFormula())
			.unit(evaluationItemDto.getUnit())
			.serviceTargets(evaluationItemDto.getServiceTargets())
			.taskTypes(taskTypeDto)
			.build();
	}
}
