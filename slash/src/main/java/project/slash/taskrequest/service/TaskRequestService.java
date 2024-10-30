package project.slash.taskrequest.service;

import static project.slash.system.exception.SystemsErrorCode.*;
import static project.slash.taskrequest.exception.TaskRequestErrorCode.*;
import static project.slash.taskrequest.model.constant.RequestStatus.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.system.model.Equipment;
import project.slash.system.repository.EquipmentRepository;
import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.StatusCountResponseDto;
import project.slash.taskrequest.dto.response.SystemCountDto;
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.response.TaskTypeCountDto;
import project.slash.taskrequest.dto.response.RequestManagerMainResponseDto;
import project.slash.taskrequest.dto.response.AllTaskTypeDto;
import project.slash.taskrequest.dto.response.RequestDetailDto;
import project.slash.taskrequest.model.TaskRequest;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.model.constant.RequestStatus;
import project.slash.taskrequest.repository.TaskRequestRepository;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskRequestService {
	private final TaskTypeRepository taskTypeRepository;
	private final TaskRequestRepository taskRequestRepository;
	private final EquipmentRepository equipmentRepository;

	public List<AllTaskTypeDto> allTaskTypes() {
		return taskTypeRepository.findAllTaskTypes();
	}

	@Transactional
	public void createRequest(TaskRequestDto taskRequestDto) {
		TaskType taskType = findTaskType(taskRequestDto);

		Equipment equipment = findEquipment(taskRequestDto);

		TaskRequest taskRequest = TaskRequest.from(taskRequestDto, taskType, null, equipment); //TODO: 유저는 로그인 기능 완료 후 넣기

		taskRequestRepository.save(taskRequest);
	}

	private Equipment findEquipment(TaskRequestDto taskRequestDto) {
		return equipmentRepository.findByName(taskRequestDto.getEquipmentName())
			.orElseThrow(() -> new BusinessException(NOT_FOUND_EQUIPMENT));
	}

	private TaskType findTaskType(TaskRequestDto taskRequestDto) {
		return taskTypeRepository.findTaskTypeByTaskRequestInfo(taskRequestDto.getType(),
			taskRequestDto.getTaskDetail(),
			taskRequestDto.isServiceRelevance()).orElseThrow(() -> new BusinessException(NOT_FOUND_TASK_TYPE));
	}

	public StatusCountResponseDto findCountByStatus(int year, int month, String user) {
		List<StatusCountDto> result = taskRequestRepository.findCountByStatus(year, month, user);

		Map<RequestStatus, Long> statusMap = new HashMap<>();
		statusMap.put(REGISTERED, 0L);
		statusMap.put(IN_PROGRESS, 0L);
		statusMap.put(COMPLETED, 0L);
		for (StatusCountDto statusCountDto : result) {
			RequestStatus status = statusCountDto.getStatus();
			statusMap.put(status, statusCountDto.getCount());
		}
		return new StatusCountResponseDto(
			statusMap.get(REGISTERED) + statusMap.get(IN_PROGRESS) + statusMap.get(COMPLETED),
			statusMap.get(REGISTERED),
			statusMap.get(IN_PROGRESS),
			statusMap.get(COMPLETED)
		);
	}

	public List<TaskTypeCountDto> findCountByTaskType(int year, int month, String user) {

		return taskRequestRepository.findCountByTaskType(year, month, user);
	}

	public List<SystemCountDto> findCountBySystem(int year, int month, String user) {

		return taskRequestRepository.findCountBySystem(year, month, user);
	}

	public RequestManagerMainResponseDto getMonthlyRequestData(int year, int month, String user) {
		StatusCountResponseDto statusCounts = findCountByStatus(year, month, user);
		List<TaskTypeCountDto> taskTypeCounts = findCountByTaskType(year, month, user);
		List<SystemCountDto> systemCounts = findCountBySystem(year, month, user);

		return new RequestManagerMainResponseDto(statusCounts, taskTypeCounts, systemCounts);
	}

	public RequestDetailDto showRequestDetail(Long requestId) {
		TaskRequest taskRequest = findRequest(requestId);

		return RequestDetailDto.from(taskRequest);
	}

	@Transactional
	public void deleteRequest(Long requestId, String userId) {
		TaskRequest request = findRequest(requestId);

		if (!request.isDeletable() && request.isRequester(userId)) {
			throw new BusinessException(CANNOT_DELETE_STARTED_TASK);
		}

		taskRequestRepository.deleteById(requestId);
	}

	private TaskRequest findRequest(Long requestId) {
		return taskRequestRepository.findById(requestId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_REQUEST));
	}
}
