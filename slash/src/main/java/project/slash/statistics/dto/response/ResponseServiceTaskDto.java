package project.slash.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.contract.model.EvaluationItem;

@Getter
@AllArgsConstructor
public class ResponseServiceTaskDto {
	private EvaluationItem evaluationItem;
	private Long taskRequest;
	private Integer totalWeight;
	private Integer dueOnTimeCount;
}
