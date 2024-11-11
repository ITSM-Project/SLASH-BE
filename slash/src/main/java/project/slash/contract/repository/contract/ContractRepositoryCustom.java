package project.slash.contract.repository.contract;

import java.util.List;
import project.slash.contract.dto.response.ContractDataDto;

public interface ContractRepositoryCustom {
	List<ContractDataDto> findContractByEvaluationItemId(long evaluationItemId, long contractId);
}
