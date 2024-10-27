package project.slash.contract.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.request.CreateContractDto;
import project.slash.contract.model.Contract;
import project.slash.contract.repository.ContractRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractService {
	private final ContractRepository contractRepository;

	@Transactional
	public void createContract(CreateContractDto createContractDto) {
		Contract contract = saveContract(createContractDto);

		contractRepository.save(contract);
	}

	private Contract saveContract(CreateContractDto createContractDto) {
		Contract contract = Contract.from(createContractDto);

		return contractRepository.save(contract);
	}
}
