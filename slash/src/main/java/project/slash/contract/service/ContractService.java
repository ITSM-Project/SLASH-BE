package project.slash.contract.service;

import static project.slash.contract.exception.ContractErrorCode.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.TaskTypeDto;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.AllContractDto;
import project.slash.contract.dto.response.ContractDto;
import project.slash.contract.dto.response.ContractDetailDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.model.Contract;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.contract.ContractRepository;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractService {
	private final TotalTargetRepository totalTargetRepository;
	private final ContractRepository contractRepository;
	private final EvaluationItemRepository evaluationItemRepository;
	private final TaskTypeRepository taskTypeRepository;

	@Transactional
	public Long createContract(ContractRequestDto contractRequestDto) {
		//기존 계약이 존재하는 경우 생성 시 기존 계약 만료 처리
		contractRepository.findByIsTerminateFalse().ifPresent(Contract::updateTerminateStatus);

		Contract contract = contractRepository.save(Contract.from(contractRequestDto));    //계약 저장

		List<TotalTarget> totalTargets = contractRequestDto.getTotalTargets().stream()
			.map(target -> TotalTarget.from(target, contract)).toList();    //종합 평가 등급 저장

		totalTargetRepository.saveAll(totalTargets);
		return contract.getId();
	}

	public ContractDetailDto showContractInfo(Long contractId) {
		ContractDto contractDto = contractRepository.findContractById(contractId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_CONTRACT));	//회사, 종합 등급 정보

		List<EvaluationItemDetailDto> evaluationItemDetails = findEvaluationItemDetails(contractId);

		return ContractDetailDto.of(contractId, contractDto, evaluationItemDetails);
	}

	private List<EvaluationItemDetailDto> findEvaluationItemDetails(Long contractId) {
		return evaluationItemRepository.findAllEvaluationItems(contractId)
			.stream()
			.map(evaluationItem -> {
				List<TaskTypeDto> taskTypes = taskTypeRepository.findTaskTypesByEvaluationItemId(evaluationItem.getEvaluationItemId())
					.stream()
					.map(TaskTypeDto::from)
					.toList();
				return EvaluationItemDetailDto.from(evaluationItem, taskTypes);
			})
			.toList();
	}

	@Transactional
	public void deleteContract(Long contractId) {
		Contract contract = findContract(contractId);

		if(LocalDate.now().isAfter(contract.getEndDate())){
			throw new BusinessException(NOT_TERMINATE_CONTRACT);
		}

		contract.updateTerminateStatus();
	}

	public List<AllContractDto> showAllContract() {
		List<Contract> allContracts = contractRepository.findAllByOrderByStartDateDesc();

		return allContracts.stream()
			.map(AllContractDto::from).toList();
	}

	private Contract findContract(Long contractId) {
		return contractRepository.findById(contractId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_CONTRACT));
	}
}
