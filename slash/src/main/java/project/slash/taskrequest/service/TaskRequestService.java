package project.slash.taskrequest.service;

import static project.slash.system.exception.SystemsErrorCode.*;
import static project.slash.taskrequest.exception.TaskRequestErrorCode.*;
import static project.slash.taskrequest.model.constant.RequestStatus.*;
import static project.slash.user.exception.UserErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.system.model.Equipment;
import project.slash.system.repository.EquipmentRepository;
import project.slash.systemincident.model.SystemIncident;
import project.slash.systemincident.repository.SystemIncidentRepository;
import project.slash.taskrequest.dto.request.RequestManagementDto;
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.request.UpdateTaskRequestManagerDto;
import project.slash.taskrequest.dto.response.RequestDetailDto;
import project.slash.taskrequest.dto.response.RequestManagementResponseDto;
import project.slash.taskrequest.dto.response.RequestManagerMainResponseDto;
import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.SystemCountDto;
import project.slash.taskrequest.dto.response.TaskRequestOfManagerDto;
import project.slash.taskrequest.dto.response.TaskTypeCountDto;
import project.slash.taskrequest.mapper.TaskRequestMapper;
import project.slash.taskrequest.model.TaskRequest;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.model.constant.RequestStatus;
import project.slash.taskrequest.repository.TaskRequestRepository;
import project.slash.taskrequest.repository.TaskTypeRepository;
import project.slash.user.model.User;
import project.slash.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskRequestService {
	private final TaskTypeRepository taskTypeRepository;
	private final TaskRequestRepository taskRequestRepository;
	private final EquipmentRepository equipmentRepository;
	private final UserRepository userRepository;
	private final SystemIncidentRepository systemIncidentRepository;

	private final TaskRequestMapper taskRequestMapper;

	@Transactional
	public void createRequest(TaskRequestDto taskRequestDto, String userId) {    //요청 생성
		TaskType taskType = findTaskType(taskRequestDto.getTaskDetail(), taskRequestDto.isServiceRelevance(),
			taskRequestDto.getContractId());

		Equipment equipment = findEquipment(taskRequestDto.getEquipmentName());

		User requester = userRepository.findById(userId).orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
		User mockUser = userRepository.findById("4").orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

		TaskRequest taskRequest = taskRequestMapper.toEntity(taskRequestDto, taskType, requester, mockUser, equipment);

		taskRequestRepository.save(taskRequest);
	}

	private TaskType findTaskType(String taskDetail, boolean isServiceRelevance, Long contractId) {
		return taskTypeRepository.findTaskTypeByTaskRequestInfo(taskDetail, isServiceRelevance, contractId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_TASK_TYPE));
	}

	private Equipment findEquipment(String equipmentName) {
		return equipmentRepository.findByName(equipmentName)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_EQUIPMENT));
	}

	public RequestDetailDto showRequestDetail(Long requestId) {	//요청 조회
		TaskRequest taskRequest = findRequest(requestId);

		return taskRequestMapper.toRequestDetailDto(taskRequest);
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
			return findTaskType(taskRequestDto.getTaskDetail(), taskRequestDto.isServiceRelevance(), taskRequestDto.getContractId());
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

	public List<StatusCountDto> findCountByStatus(int year, int month, String user,Long contractId) {
		return taskRequestRepository.findCountByStatus(year, month, user,contractId);
	}

	public List<TaskTypeCountDto> findCountByTaskType(int year, int month, String user,Long contractId) {
		return taskRequestRepository.findCountByTaskType(year, month, user,contractId);
	}

	public List<SystemCountDto> findCountBySystem(int year, int month, String user,Long contractId) {
		return taskRequestRepository.findCountBySystem(year, month, user,contractId);
	}

	public RequestManagerMainResponseDto getMonthlyRequestData(int year, int month, String user,Long contractId) {
		List<StatusCountDto> statusCounts = findCountByStatus(year, month, user,contractId);
		List<TaskTypeCountDto> taskTypeCounts = findCountByTaskType(year, month, user,contractId);
		List<SystemCountDto> systemCounts = findCountBySystem(year, month, user,contractId);

		return new RequestManagerMainResponseDto(statusCounts, taskTypeCounts, systemCounts);
	}

	public List<TaskRequestOfManagerDto> getTaskRequestOfManager() {
		return taskRequestRepository.findTaskRequestOfManager();
	}

	public RequestManagementResponseDto findFilteredRequests(String equipmentName, String type, String taskDetail,
		RequestStatus status, String keyword,
		Pageable pageable, Integer year, Integer month, Long contractId, String userId
	) {

		User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

		Page<RequestManagementDto> taskResponseRequestDtos = taskRequestRepository.findFilteredRequests(
			equipmentName, type, taskDetail, status, keyword, pageable, year, month, contractId,
			user.getId(), user.getRole());

		return new RequestManagementResponseDto(
			taskResponseRequestDtos.getContent(),
			taskResponseRequestDtos.getTotalPages(),
			taskResponseRequestDtos.getNumber() + 1,
			taskResponseRequestDtos.getTotalElements()
		);
	}

	@Transactional
	public void allocateRequest(UpdateTaskRequestManagerDto updateTaskRequestManagerDto) {
		taskRequestRepository.updateManagerByRequestId(updateTaskRequestManagerDto.getRequestId(),
			updateTaskRequestManagerDto.getManagerId());// 저장
	}

	@Transactional
	public void completeRequest(long requestId, String rManagerId) {
		TaskRequest taskRequest = taskRequestRepository.findById(requestId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_REQUEST));

		taskRequestRepository.updateDueOnTime(requestId, rManagerId, COMPLETED);
		if (taskRequest.getTaskType().getType().equals("장애 요청")) {
			Long duration = taskRequestRepository.getDuration(requestId);
			SystemIncident systemIncident = SystemIncident.create(duration, taskRequest);
			systemIncidentRepository.save(systemIncident);
		}
	}

	public List<StatusCountDto> getStatusCountByUser(int year, int month, String user,Long contractId){
		return taskRequestRepository.findStatusCountByUser(year, month, user,contractId);
	}

}
