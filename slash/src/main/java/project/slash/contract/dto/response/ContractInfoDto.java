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
			getCategories(contract),
			getTotalTargets(contract)
		);

		return new ContractInfoDto(contractDto, evaluationItems);
	}

	private static List<GradeDto> getTotalTargets(Contract contract) {
		return contract.getTotalTargets().stream()
			.map(GradeDto::createTotalGradeDto)
			.toList();
	}

	private static List<String> getCategories(Contract contract) {
		return contract.getEvaluationItems().stream()
			.map(EvaluationItem::getCategory)
			.toList();
	}
}
