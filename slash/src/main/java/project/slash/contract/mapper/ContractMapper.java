package project.slash.contract.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.AllContractDto;
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
			.map(AllContractDto::from)
			.toList();
	}
}
