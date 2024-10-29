package project.slash.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.request.CreateContractDto;
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
	public void createContract(CreateContractDto createContractDto) {
		Contract contract = Contract.from(createContractDto);

		contractRepository.save(contract);
	}

	public ContractInfoDto showContractInfo() {
		Contract contract = contractRepository.findContractsNotTerminate();

		List<EvaluationItemDto> evaluationItemInfos = evaluationItemRepository.findEvaluationItemInfos(
			contract.getId());

		return ContractInfoDto.of(contract, evaluationItemInfos);
	}
}
