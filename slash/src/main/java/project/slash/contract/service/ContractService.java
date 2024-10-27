package project.slash.contract.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractService {
	// private final ContractRepository contractRepository;
	// private final EvaluationItemsRepository evaluationItemsRepository;
	//
	// @Transactional
	// public void createContract(CreateContractDto createContractDto) {
	// 	Contract contract = saveContract(createContractDto);
	//
	// 	List<EvaluationItems> evaluationItems = createContractDto.getEvaluationItems().stream()
	// 		.map(item -> EvaluationItems.of(item, contract))
	// 		.toList();
	//
	// 	evaluationItemsRepository.saveAll(evaluationItems);
	// }
	//
	// private Contract saveContract(CreateContractDto createContractDto) {
	// 	Contract contract = Contract.from(createContractDto);
	//
	// 	return contractRepository.save(contract);
	// }
}
