package project.slash.contract.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.TaskTypeDto;

@Getter
@Builder
public class CreateEvaluationItemDto {
	@NotNull(message = "계약 아이디는 필수입니다.")
	private Long contractId;

	@NotBlank(message = "서비스 평가 항목은 필수입니다.")
	private String category;

	@NotNull(message = "가중치는 필수입니다.")
	private int weight;

	@NotBlank(message = "측정 주기는 필수입니다.")
	private String period;

	@NotBlank(message = "목적은 필수입니다.")
	private String purpose;

	@NotBlank(message = "산출식은 필수입니다.")
	private String formula;

	@NotBlank(message = "측정단위는 필수입니다.")
	private String unit;

	@Valid
	private List<TaskTypeDto> taskTypes;

	@Valid
	@NotNull(message = "서비스 목표는 필수입니다.")
	private List<GradeDto> serviceTargets;
}
