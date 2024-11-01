package project.slash.contract.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.ServiceTargetDto;
import project.slash.contract.dto.TaskTypeDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluationItemDetailDto {
	private Long evaluationItemId;

	private String category;    //서비스 항목

	private Integer weight;

	private String period;

	private String purpose;

	private String formula;

	private String unit;

	private List<ServiceTargetDto> serviceTargets;

	private List<TaskTypeDto> taskTypes;

	public static EvaluationItemDetailDto from(EvaluationItemDto evaluationItemDto, List<TaskTypeDto> taskTypeDto) {
		return new EvaluationItemDetailDto(
			evaluationItemDto.getEvaluationItemId(),
			evaluationItemDto.getCategory(),
			evaluationItemDto.getWeight(),
			evaluationItemDto.getPeriod(),
			evaluationItemDto.getPurpose(),
			evaluationItemDto.getFormula(),
			evaluationItemDto.getUnit(),
			evaluationItemDto.getServiceTargets(),
			taskTypeDto);
	}
}
