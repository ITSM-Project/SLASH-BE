package project.slash.evaluationitem.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.evaluationitem.dto.ServiceTargetDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluationItemDto {
	private Long evaluationItemId;

	private String category;    //서비스 항목

	private Integer weight;

	private String period;

	private String purpose;

	private String formula;

	private String unit;

	private List<ServiceTargetDto> serviceTargets;
}