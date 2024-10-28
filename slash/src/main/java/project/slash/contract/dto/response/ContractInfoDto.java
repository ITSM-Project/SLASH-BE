package project.slash.contract.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.model.Contract;
import project.slash.contract.model.EvaluationItem;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContractInfoDto {
	private Long contractId;
	private ContractDto contract;
	private List<EvaluationItemDto> evaluationItemInfos;

	public static ContractInfoDto of(Contract contract, List<EvaluationItemDto> evaluationItems) {
		ContractDto showContractDto = new ContractDto(
			contract.getCompanyName(),
			contract.getStartDate(),
			contract.getEndDate(),
			contract.getEvaluationItems().stream()
				.map(EvaluationItem::getCategory)
				.toList(),
			contract.getTotalTargets().stream()
				.map(GradeDto::createTotalGradeDto)
				.toList()
		);

		return new ContractInfoDto(contract.getId(), showContractDto, evaluationItems);
	}
}
