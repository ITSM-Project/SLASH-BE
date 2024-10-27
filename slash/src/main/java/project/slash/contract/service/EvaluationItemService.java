package project.slash.contract.service;

import static project.slash.contract.exception.ContractErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.request.DetailDto;
import project.slash.contract.dto.request.GradeDto;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.ServiceDetail;
import project.slash.contract.model.ServiceTarget;
import project.slash.contract.repository.EvaluationItemRepository;
import project.slash.contract.repository.ServiceDetailRepository;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.taskrequest.dto.request.CreateTaskTypeDto;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
public class EvaluationItemService {
	private final EvaluationItemRepository evaluationItemRepository;
	private final ServiceDetailRepository serviceDetailRepository;
	private final ServiceTargetRepository serviceTargetRepository;
	private final TaskTypeRepository taskTypeRepository;

	@Transactional
	public void createDetail(DetailDto detailDto) {
		EvaluationItem evaluationItem = evaluationItemRepository.findById(detailDto.getCategoryId())
			.orElseThrow(() -> new BusinessException(NOT_FOUND_ITEMS));

		ServiceDetail serviceDetail = ServiceDetail.from(detailDto, evaluationItem);

		serviceDetailRepository.save(serviceDetail);
		saveServiceTargets(detailDto.getServiceTargets(), evaluationItem);
		saveTaskTypes(detailDto.getTaskTypes(), evaluationItem);
	}

	private void saveTaskTypes(List<CreateTaskTypeDto> types, EvaluationItem evaluationItem) {
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
}
