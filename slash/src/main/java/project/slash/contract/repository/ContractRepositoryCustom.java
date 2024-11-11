package project.slash.contract.repository;

import java.util.List;
import java.util.Optional;

import project.slash.contract.dto.response.ContractDataDto;
import project.slash.contract.dto.response.ContractDto;

public interface ContractRepositoryCustom {
	Optional<ContractDto> findContractById(Long contractId);

	List<ContractDataDto> findIndicatorByCategory(String category);

	List<ContractDataDto> findContractByEvaluationItemId(long evaluationItemId, long contractId);
}
