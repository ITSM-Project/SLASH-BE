package project.slash.contract.service;

import static project.slash.contract.exception.ContractErrorCode.*;
import static project.slash.contract.exception.EvaluationItemErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.model.Contract;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.ServiceTarget;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.dto.ServiceTargetDto;
import project.slash.contract.dto.TaskTypeDto;
import project.slash.contract.dto.request.CreateEvaluationItemDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.dto.response.EvaluationItemDto;
import project.slash.contract.repository.EvaluationItemRepository;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
public class EvaluationItemService {
	private final ContractRepository contractRepository;
	private final EvaluationItemRepository evaluationItemRepository;
	private final ServiceTargetRepository serviceTargetRepository;
	private final TaskTypeRepository taskTypeRepository;

	@Transactional
	public void createEvaluationItem(CreateEvaluationItemDto createEvaluationItemDto) {
		Contract contract = contractRepository.findById(createEvaluationItemDto.getContractId())
			.orElseThrow(() -> new BusinessException(NOT_FOUND_CONTRACT));

		EvaluationItem evaluationItem = saveEvaluationItem(createEvaluationItemDto, contract);	//서비스 평가 항목 저장
		saveServiceTargets(createEvaluationItemDto.getServiceTargets(), evaluationItem);	//서비스 목표 저장
		saveTaskTypes(createEvaluationItemDto.getTaskTypes(), evaluationItem);	//업무 유형 저장
	}

	private EvaluationItem saveEvaluationItem(CreateEvaluationItemDto createEvaluationItemDto, Contract contract) {
		EvaluationItem evaluationItem = EvaluationItem.from(createEvaluationItemDto, contract);
		return evaluationItemRepository.save(evaluationItem);
	}

	private void saveTaskTypes(List<TaskTypeDto> types, EvaluationItem evaluationItem) {
		List<TaskType> taskTypes = types.stream()
			.map(taskType -> TaskType.from(taskType, evaluationItem))
			.toList();
		taskTypeRepository.saveAll(taskTypes);
	}

	private void saveServiceTargets(List<ServiceTargetDto> targets, EvaluationItem evaluationItem) {
		List<ServiceTarget> serviceTargets = targets.stream()
			.map(target -> ServiceTarget.from(target, evaluationItem))
			.toList();
		serviceTargetRepository.saveAll(serviceTargets);
	}

	public EvaluationItemDetailDto findDetailByItemId(Long evaluationItemId) {
		EvaluationItemDto evaluationItemDto = evaluationItemRepository.findEvaluationItem(evaluationItemId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_ITEMS));

		List<TaskTypeDto> taskTypes = taskTypeRepository.findTaskTypesByEvaluationItemId(evaluationItemId).stream()
			.map(TaskTypeDto::from).toList();

		return EvaluationItemDetailDto.from(evaluationItemDto, taskTypes);
	}
}
