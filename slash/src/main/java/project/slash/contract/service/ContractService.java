package project.slash.contract.service;

import static project.slash.contract.exception.ContractErrorCode.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.TaskTypeDto;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.AllContractDto;
import project.slash.contract.dto.response.ContractDetailDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.mapper.ContractMapper;
import project.slash.contract.mapper.TotalTargetMapper;
import project.slash.contract.model.Contract;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.taskrequest.mapper.TaskTypeMapper;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractService {
	private final TotalTargetRepository totalTargetRepository;
	private final ContractRepository contractRepository;
	private final EvaluationItemRepository evaluationItemRepository;
	private final TaskTypeRepository taskTypeRepository;

	private final ContractMapper contractMapper;
	private final TotalTargetMapper totalTargetMapper;
	private final TaskTypeMapper taskTypeMapper;

	@Transactional
	public Long createContract(ContractRequestDto contractRequestDto) {
		Contract contract = contractRepository.save(contractMapper.toEntity(contractRequestDto));

		List<TotalTarget> totalTargets = totalTargetMapper.toTotalTargetList(contractRequestDto.getTotalTargets(), contract);

		totalTargetRepository.saveAll(totalTargets);
		return contract.getId();
	}

	public ContractDetailDto showContractInfo(Long contractId) {
		Contract contract = contractRepository.findById(contractId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_CONTRACT));

		List<GradeDto> totalTargets = totalTargetMapper.toGradeDtoList(
			totalTargetRepository.findByContractId(contract.getId()));

		List<EvaluationItemDetailDto> evaluationItemDetails = findEvaluationItemDetails(contractId);

		return ContractDetailDto.of(contract, totalTargets, evaluationItemDetails);
	}

	private List<EvaluationItemDetailDto> findEvaluationItemDetails(Long contractId) {
		return evaluationItemRepository.findAllEvaluationItems(contractId)
			.stream()
			.map(evaluationItem -> {
				List<TaskTypeDto> taskTypes = taskTypeMapper.toTaskTypeDtoList(
					taskTypeRepository.findTaskTypesByEvaluationItemId(evaluationItem.getEvaluationItemId()));

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

		return contractMapper.toAllContractDtoList(allContracts);
	}

	private Contract findContract(Long contractId) {
		return contractRepository.findById(contractId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_CONTRACT));
	}
}
