package project.slash.contract.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.GradeDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContractDetailDto {
	private Long contractId;

	private String companyName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private Boolean isTerminate;

	private List<GradeDto> totalTargets;

	private List<EvaluationItemDetailDto> evaluationItems;

	public static ContractDetailDto of(Long contractId, ContractDto contractDto, List<EvaluationItemDetailDto> evaluationItems) {
		return new ContractDetailDto(
			contractId,
			contractDto.getCompanyName(),
			contractDto.getStartDate(),
			contractDto.getEndDate(),
			contractDto.isTerminate(),
			contractDto.getTotalTargets(),
			evaluationItems
		);
	}
}
