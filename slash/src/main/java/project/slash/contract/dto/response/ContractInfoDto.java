package project.slash.contract.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.contract.dto.ContractDto;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.model.Contract;
import project.slash.contract.model.EvaluationItem;

@Getter
@AllArgsConstructor
public class ContractInfoDto {
	private ContractDto contract;
	private List<EvaluationItemDto> evaluationItemInfos;

	public static ContractInfoDto of(Contract contract, List<EvaluationItemDto> evaluationItems) {
		ContractDto contractDto = new ContractDto(
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

		return new ContractInfoDto(contractDto, evaluationItems);
	}
}
