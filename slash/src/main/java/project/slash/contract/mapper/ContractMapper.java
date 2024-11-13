package project.slash.contract.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.AllContractDto;
import project.slash.contract.dto.response.ContractDetailDto;
import project.slash.contract.dto.response.ContractNameDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.model.Contract;

@Component
public class ContractMapper {
	public Contract toEntity(ContractRequestDto dto) {
		return Contract.builder()
			.contractName(dto.getContractName())
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.build();
	}

	public List<AllContractDto> toAllContractDtoList(List<Contract> contracts) {
		return contracts.stream()
			.map(this::toAllContractDto)
			.toList();
	}

	public List<ContractNameDto> toAllContractNameList(List<Contract> allContracts) {
		return allContracts.stream()
			.map(contract -> ContractNameDto.of(contract.getId(), contract.getContractName()))
			.toList();
	}

	public AllContractDto toAllContractDto(Contract contract) {
		return new AllContractDto(contract.getId(), contract.getContractName(), contract.getStartDate(),
			contract.getEndDate(), contract.isTerminate());
	}

	public ContractDetailDto toContractDetailDto(Contract contract, List<GradeDto> totalTargets, List<EvaluationItemDetailDto> evaluationItems) {
		return new ContractDetailDto(
			contract.getId(),
			contract.getContractName(),
			contract.getStartDate(),
			contract.getEndDate(),
			contract.isTerminate(),
			totalTargets,
			evaluationItems
		);
	}
}
