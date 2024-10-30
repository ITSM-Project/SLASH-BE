package project.slash.contract.service;

import static project.slash.contract.exception.ContractErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.TaskTypeDto;
import project.slash.contract.dto.request.CreateDetailDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.ServiceTarget;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
public class EvaluationItemService {
	private final EvaluationItemRepository evaluationItemRepository;
	private final ServiceTargetRepository serviceTargetRepository;
	private final TaskTypeRepository taskTypeRepository;

	@Transactional
	public void createDetail(CreateDetailDto detailDto) {
		EvaluationItem evaluationItem = evaluationItemRepository.findById(detailDto.getCategoryId())
			.orElseThrow(() -> new BusinessException(NOT_FOUND_ITEMS));

		saveServiceTargets(detailDto.getServiceTargets(), evaluationItem);
		saveTaskTypes(detailDto.getTaskTypes(), evaluationItem);
	}

	private void saveTaskTypes(List<TaskTypeDto> types, EvaluationItem evaluationItem) {
		List<TaskType> taskTypes = types.stream()
			.map(taskType -> TaskType.from(taskType, evaluationItem))
			.toList();
		taskTypeRepository.saveAll(taskTypes);
	}

	private void saveServiceTargets(List<GradeDto> targets, EvaluationItem evaluationItem) {
		List<ServiceTarget> serviceTargets = targets.stream()
			.map(target -> ServiceTarget.from(target, evaluationItem))
			.toList();
		serviceTargetRepository.saveAll(serviceTargets);
	}

	public EvaluationItemDetailDto findDetailByCategoryId(Long categoryId) {
		EvaluationItemDetailDto evaluationItemDetail = evaluationItemRepository.findEvaluationItemDetail(categoryId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_ITEMS));

		List<TaskTypeDto> taskTypes = taskTypeRepository.findTaskTypesByEvaluationItemId(categoryId).stream()
			.map(TaskTypeDto::from).toList();

		return evaluationItemDetail.withTaskTypes(taskTypes);
	}
}
