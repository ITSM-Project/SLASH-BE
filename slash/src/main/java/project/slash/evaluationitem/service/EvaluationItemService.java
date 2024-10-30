package project.slash.evaluationitem.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.GradeDto;
import project.slash.evaluationitem.dto.ServiceTargetDto;
import project.slash.evaluationitem.dto.TaskTypeDto;
import project.slash.evaluationitem.dto.request.CreateEvaluationItemDto;
import project.slash.evaluationitem.model.EvaluationItem;
import project.slash.evaluationitem.model.ServiceTarget;
import project.slash.evaluationitem.repository.EvaluationItemRepository;
import project.slash.evaluationitem.repository.ServiceTargetRepository;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
public class EvaluationItemService {
	private final EvaluationItemRepository evaluationItemRepository;
	private final ServiceTargetRepository serviceTargetRepository;
	private final TaskTypeRepository taskTypeRepository;

	@Transactional
	public void createEvaluationItem(CreateEvaluationItemDto createEvaluationItemDto) {
		EvaluationItem evaluationItem = EvaluationItem.from(createEvaluationItemDto);
		evaluationItemRepository.save(evaluationItem); //서비스 평가 항목 저장

		saveServiceTargets(createEvaluationItemDto.getServiceTargets(), evaluationItem);	//서비스 목표 저장
		saveTaskTypes(createEvaluationItemDto.getTaskTypes(), evaluationItem);	//업무 유형 저장
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

	// public EvaluationItemDetailDto findDetailByCategoryId(Long categoryId) {
	// 	// EvaluationItemDetailDto evaluationItemDetail = evaluationItemRepository.findEvaluationItemDetail(categoryId)
	// 	// 	.orElseThrow(() -> new BusinessException(NOT_FOUND_ITEMS));
	//
	// 	List<TaskTypeDto> taskTypes = taskTypeRepository.findTaskTypesByEvaluationItemId(categoryId).stream()
	// 		.map(TaskTypeDto::from).toList();
	//
	// 	return evaluationItemDetail.withTaskTypes(taskTypes);
	// }
}
