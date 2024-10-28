package project.slash.contract.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import project.slash.contract.dto.GradeDto;

@Getter
public class CreateContractDto {
	@NotBlank(message = "회사 이름은 필수입니다.")
	String companyName;
	@NotNull(message = "계약 시작일은 필수입니다.")
	LocalDate startDate;
	@NotNull(message = "계약 종료일은 필수입니다.")
	LocalDate endDate;
	@NotNull(message = "서비스 평가 항목은 필수입니다.")
	List<String> categories;
	@Valid @NotNull(message = "종합 평가 점수 기준은 필수입니다.")
	List<GradeDto> totalTargets;
}
