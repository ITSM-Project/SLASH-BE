package project.slash.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.request.CreateContractDto;
import project.slash.contract.dto.response.ContractInfoDto;
import project.slash.contract.dto.response.EvaluationItemDto;
import project.slash.contract.model.Contract;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractService {
	private final TotalTargetRepository totalTargetRepository;
	private final ContractRepository contractRepository;
	private final EvaluationItemRepository evaluationItemRepository;

	@Transactional
	public void createContract(CreateContractDto createContractDto) {
		//TODO: 만료되지 않은 계약에 대해서 2개 이상 생성 불가능 하도록 해야함
		Contract contract = contractRepository.save(Contract.from(createContractDto));	//계약 저장

		List<TotalTarget> totalTargets = createContractDto.getTotalTargets().stream()
			.map(target -> TotalTarget.from(target, contract)).toList();	//종합 평가 등급 저장

		totalTargetRepository.saveAll(totalTargets);
	}

	public ContractInfoDto showContractInfo() {
		Contract contract = contractRepository.findContractsNotTerminate();

		List<EvaluationItemDto> evaluationItemInfos = evaluationItemRepository.findEvaluationItemInfos(
			contract.getId());

		return ContractInfoDto.of(contract, evaluationItemInfos);
	}
}
