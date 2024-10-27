package project.slash.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.ContractDto;
import project.slash.contract.dto.response.ContractInfoDto;
import project.slash.contract.dto.response.EvaluationItemDto;
import project.slash.contract.model.Contract;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractService {
	private final ContractRepository contractRepository;
	private final EvaluationItemRepository evaluationItemRepository;

	@Transactional
	public void createContract(ContractDto contractDto) {
		Contract contract = saveContract(contractDto);

		contractRepository.save(contract);
	}

	private Contract saveContract(ContractDto contractDto) {
		Contract contract = Contract.from(contractDto);

		return contractRepository.save(contract);
	}

	public ContractInfoDto showContractInfo() {
		Contract contract = contractRepository.findContractsNotTerminate();

		List<EvaluationItemDto> evaluationItemInfos = evaluationItemRepository.findEvaluationItemInfos(
			contract.getId());

		return ContractInfoDto.of(contract, evaluationItemInfos);
	}
}
