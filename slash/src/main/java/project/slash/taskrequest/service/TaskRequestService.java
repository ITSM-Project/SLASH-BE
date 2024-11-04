package project.slash.taskrequest.service;

import static project.slash.system.exception.SystemsErrorCode.*;
import static project.slash.taskrequest.exception.TaskRequestErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.system.model.Equipment;
import project.slash.system.repository.EquipmentRepository;
import project.slash.taskrequest.dto.request.RequestManagementDto;
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.response.RequestDetailDto;
import project.slash.taskrequest.dto.response.RequestManagementResponseDto;
import project.slash.taskrequest.dto.response.RequestManagerMainResponseDto;
import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.SystemCountDto;
import project.slash.taskrequest.dto.response.TaskRequestOfManagerDto;
import project.slash.taskrequest.dto.response.TaskTypeCountDto;
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


	@Transactional
	public void createRequest(TaskRequestDto taskRequestDto) {    //요청 생성
		TaskType taskType = findTaskType(taskRequestDto.getTaskDetail(), taskRequestDto.isServiceRelevance());

		Equipment equipment = findEquipment(taskRequestDto.getEquipmentName());

		TaskRequest taskRequest = TaskRequest.from(taskRequestDto, taskType, null,
			equipment); //TODO: 유저는 로그인 기능 완료 후 넣기

		taskRequestRepository.save(taskRequest);
	}

	private TaskType findTaskType(String taskDetail, boolean isServiceRelevance) {
		return taskTypeRepository.findTaskTypeByTaskRequestInfo(taskDetail, isServiceRelevance)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_TASK_TYPE));
	}

	private Equipment findEquipment(String equipmentName) {
		return equipmentRepository.findByName(equipmentName)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_EQUIPMENT));
	}

	public RequestDetailDto showRequestDetail(Long requestId) {	//요청 조회
		TaskRequest taskRequest = findRequest(requestId);

		return RequestDetailDto.from(taskRequest);
	}

	@Transactional
	public void deleteRequest(Long requestId, String userId) {	//요청 삭제
		TaskRequest request = findRequest(requestId);

		validRequest(userId, request);

		taskRequestRepository.deleteById(requestId);
	}

	@Transactional
	public void editRequest(Long requestId, String userId, TaskRequestDto taskRequestDto) {	//요청 수정
		TaskRequest request = findRequest(requestId);
		validRequest(userId, request);

		TaskType taskType = getEditTaskType(taskRequestDto);
		Equipment equipment = getEditEquipment(taskRequestDto);

		request.update(taskRequestDto, taskType, equipment);
	}

	private Equipment getEditEquipment(TaskRequestDto taskRequestDto) {
		if(taskRequestDto.getEquipmentName() != null) {
			return findEquipment(taskRequestDto.getEquipmentName());
		}
		return null;
	}

	private TaskType getEditTaskType(TaskRequestDto taskRequestDto) {
		if(taskRequestDto.getTaskDetail() != null) {
			return findTaskType(taskRequestDto.getTaskDetail(), taskRequestDto.isServiceRelevance());
		}
		return null;
	}

	private void validRequest(String userId, TaskRequest request) {
		if(!request.isRequester(userId)){
			throw new BusinessException(NOT_REQUEST_OWNER);
		}

		if (!request.isDeletable()) {
			throw new BusinessException(CANNOT_DELETE_OR_EDIT_REQUEST);
		}
	}

	private TaskRequest findRequest(Long requestId) {
		return taskRequestRepository.findById(requestId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_REQUEST));
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

	public List<TaskRequestOfManagerDto> getTaskRequestOfManager() {
		return taskRequestRepository.findTaskRequestOfManager();
	}

	public RequestManagementResponseDto findFilteredRequests(
		String equipmentName,
		String type,
		String taskDetail,
		RequestStatus status,
		String keyword,
		Pageable pageable
	) {
		Page<RequestManagementDto> taskResponseRequestDtos = taskRequestRepository.findFilteredRequests(
			equipmentName, type, taskDetail, status, keyword, pageable);

		return new RequestManagementResponseDto(
			taskResponseRequestDtos.getContent(),
			taskResponseRequestDtos.getTotalPages(),
			taskResponseRequestDtos.getNumber() + 1,
			taskResponseRequestDtos.getTotalElements()
		);
	}

}
