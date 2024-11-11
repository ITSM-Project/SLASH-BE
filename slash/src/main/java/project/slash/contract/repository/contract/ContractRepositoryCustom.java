package project.slash.contract.repository.contract;

import java.util.List;

import project.slash.contract.dto.ContractDataDto;

public interface ContractRepositoryCustom {
	List<ContractDataDto> findIndicatorByCategory(String category);
}
