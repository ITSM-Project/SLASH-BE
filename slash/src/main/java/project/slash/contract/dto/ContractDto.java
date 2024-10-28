package project.slash.contract.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContractDto {
	@NotBlank(message = "회사 이름은 필수입니다.")
	private String companyName;

	@NotNull(message = "계약 시작일은 필수입니다.")
	private LocalDate startDate;

	@NotNull(message = "계약 종료일은 필수입니다.")
	private LocalDate endDate;

	@NotNull(message = "서비스 평가 항목은 필수입니다.")
	private List<String> categories;

	@Valid
	@NotNull(message = "종합 평가 점수 기준은 필수입니다.")
	private List<GradeDto> totalTargets;
}
