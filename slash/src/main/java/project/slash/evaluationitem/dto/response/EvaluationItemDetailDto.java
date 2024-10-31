package project.slash.evaluationitem.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.evaluationitem.dto.TaskTypeDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluationItemDetailDto {
	private EvaluationItemDto evaluationItems;
	private List<TaskTypeDto> taskTypes;
}
