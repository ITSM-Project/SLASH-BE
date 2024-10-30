package project.slash.contract.repository.contract;

import java.util.Optional;

import project.slash.contract.dto.response.ContractDto;

public interface ContractRepositoryCustom {
	Optional<ContractDto> findContractById(Long contractId);
}
