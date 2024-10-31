package project.slash.taskrequest.service;

import static project.slash.system.exception.SystemsErrorCode.*;
import static project.slash.taskrequest.exception.TaskRequestErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.system.model.Equipment;
import project.slash.system.repository.EquipmentRepository;
import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.SystemCountDto;
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.response.TaskTypeCountDto;
import project.slash.taskrequest.dto.response.RequestManagerMainResponseDto;
import project.slash.taskrequest.dto.response.AllTaskTypeDto;
import project.slash.taskrequest.dto.response.RequestDetailDto;
import project.slash.taskrequest.model.TaskRequest;
import project.slash.taskrequest.model.TaskType;
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

		TaskRequest taskRequest = TaskRequest.from(taskRequestDto, taskType, null,
			equipment); //TODO: 유저는 로그인 기능 완료 후 넣기

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

	public List<StatusCountDto> findCountByStatus(int year, int month, String user){
		return taskRequestRepository.findCountByStatus(year, month, user);
	}

	public List<TaskTypeCountDto> findCountByTaskType(int year, int month, String user) {

		return taskRequestRepository.findCountByTaskType(year, month, user);
	}

	public List<SystemCountDto> findCountBySystem(int year, int month, String user) {

		return taskRequestRepository.findCountBySystem(year, month, user);
	}

	public RequestManagerMainResponseDto getMonthlyRequestData(int year, int month, String user) {
		List<StatusCountDto> statusCounts = findCountByStatus(year, month, user);
		List<TaskTypeCountDto> taskTypeCounts = findCountByTaskType(year, month, user);
		List<SystemCountDto> systemCounts = findCountBySystem(year, month, user);

		return new RequestManagerMainResponseDto(statusCounts, taskTypeCounts, systemCounts);
	}

	public RequestDetailDto showRequestDetail(Long requestId) {
		TaskRequest taskRequest = taskRequestRepository.findById(requestId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_REQUEST));

		return RequestDetailDto.from(taskRequest);
	}
}
