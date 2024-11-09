package project.slash.contract.service;

import static project.slash.contract.exception.ContractErrorCode.*;
import static project.slash.contract.exception.EvaluationItemErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.mapper.ServiceTargetMapper;
import project.slash.contract.model.Contract;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.ServiceTarget;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.dto.TaskTypeDto;
import project.slash.contract.dto.request.CreateEvaluationItemDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.dto.response.EvaluationItemDto;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.statistics.model.Statistics;
import project.slash.statistics.repository.StatisticsRepository;
import project.slash.taskrequest.mapper.TaskTypeMapper;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
public class EvaluationItemService {
	private final ContractRepository contractRepository;
	private final EvaluationItemRepository evaluationItemRepository;
	private final ServiceTargetRepository serviceTargetRepository;
	private final TaskTypeRepository taskTypeRepository;
	private final StatisticsRepository statisticsRepository;

	private final TaskTypeMapper taskTypeMapper;
	private final ServiceTargetMapper serviceTargetMapper;

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

	public EvaluationItemDetailDto findDetailByItemId(Long evaluationItemId) {
		EvaluationItemDto evaluationItemDto = evaluationItemRepository.findEvaluationItem(evaluationItemId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_ITEMS));

		List<TaskTypeDto> taskTypes = taskTypeMapper.toTaskTypeDtoList(
			taskTypeRepository.findTaskTypesByEvaluationItemId(evaluationItemId));

		return EvaluationItemDetailDto.createAll(evaluationItemDto, taskTypes);
	}

	public boolean checkModifiable(Long contractId) {
		List<Long> evaluationItemIds = evaluationItemRepository.findIdsByContractId(contractId);
		List<Statistics> statisticsList = statisticsRepository.findByEvaluationItems_IdIn(evaluationItemIds);

		return statisticsList.isEmpty();
	}

	@Transactional
	public void updateEvaluationItem(Long evaluationItemId, CreateEvaluationItemDto newEvaluationItem) {
		EvaluationItem evaluationItem = evaluationItemRepository.findById(evaluationItemId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_ITEMS));

		evaluationItem.update(newEvaluationItem);	//서비스 평가 항목 내용 수정

		if(!newEvaluationItem.getServiceTargets().isEmpty()) {
			updateServiceTargets(evaluationItemId, newEvaluationItem.getServiceTargets(), evaluationItem);
		}

		if(!newEvaluationItem.getTaskTypes().isEmpty()) {
			updateTaskTypes(evaluationItemId, newEvaluationItem.getTaskTypes(), evaluationItem);
		}
	}

	private void updateTaskTypes(Long evaluationItemId, List<TaskTypeDto> newTaskTypes, EvaluationItem evaluationItem) {
		List<TaskType> taskTypes = taskTypeRepository.findTaskTypesByEvaluationItemId(evaluationItemId);
		taskTypeRepository.deleteAll(taskTypes);	//기존 등급 삭제

		saveTaskTypes(newTaskTypes, evaluationItem);	//새 등급 저장
	}

	private void updateServiceTargets(Long evaluationItemId, List<GradeDto> newServiceTargets, EvaluationItem evaluationItem) {
		List<ServiceTarget> serviceTargets = serviceTargetRepository.findByEvaluationItemId(evaluationItemId);
		serviceTargetRepository.deleteAll(serviceTargets);	//기존 등급 삭제

		saveServiceTargets(newServiceTargets, evaluationItem);	//새 등급 저장
	}

	private void saveTaskTypes(List<TaskTypeDto> types, EvaluationItem evaluationItem) {
		List<TaskType> taskTypes = taskTypeMapper.toTaskTypeList(types, evaluationItem);
		taskTypeRepository.saveAll(taskTypes);
	}

	private void saveServiceTargets(List<GradeDto> targets, EvaluationItem evaluationItem) {
		List<ServiceTarget> serviceTargets = serviceTargetMapper.toServiceTargetList(targets, evaluationItem);
		serviceTargetRepository.saveAll(serviceTargets);
	}
}
