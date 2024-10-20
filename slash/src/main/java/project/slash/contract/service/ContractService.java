package project.slash.contract.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.request.CreateContractDto;
import project.slash.contract.model.Contract;
import project.slash.contract.repository.ContractRepository;

@Service
@RequiredArgsConstructor
public class ContractService {
	private final ContractRepository contractRepository;

	public Long createContract(CreateContractDto createContractDto) {
		Contract contract = Contract.createOf(createContractDto);

		return contractRepository.save(contract).getId();
	}
}
