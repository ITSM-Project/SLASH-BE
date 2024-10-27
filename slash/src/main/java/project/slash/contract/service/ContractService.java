package project.slash.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.request.CreateContractDto;
import project.slash.contract.model.Contract;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.model.EvaluationItems;
import project.slash.contract.repository.EvaluationItemsRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractService {
	private final ContractRepository contractRepository;
	private final EvaluationItemsRepository evaluationItemsRepository;

	@Transactional
	public void createContract(CreateContractDto createContractDto) {
		Contract contract = saveContract(createContractDto);

		List<EvaluationItems> evaluationItems = createContractDto.getEvaluationItems().stream()
			.map(item -> EvaluationItems.of(item, contract))
			.toList();

		evaluationItemsRepository.saveAll(evaluationItems);
	}

	private Contract saveContract(CreateContractDto createContractDto) {
		Contract contract = Contract.from(createContractDto);

		return contractRepository.save(contract);
	}
}
