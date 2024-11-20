package project.slash.taskrequest.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.security.annotation.Login;
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.request.UpdateTaskRequestManagerDto;
import project.slash.taskrequest.dto.response.RequestManagementResponseDto;
import project.slash.taskrequest.dto.response.RequestManagerMainResponseDto;
import project.slash.taskrequest.dto.response.RequestDetailDto;
import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.TaskRequestOfManagerDto;
import project.slash.taskrequest.model.constant.RequestStatus;
import project.slash.taskrequest.service.TaskRequestService;
import project.slash.user.repository.UserRepository;

@RestController
@RequiredArgsConstructor
public class TaskRequestController {
	private final TaskRequestService taskRequestService;
	private final UserRepository userRepository;

	/**
	 * 요청 생성 메서드입니다.
	 *
	 * @param taskRequestDto 요청 정보
	 * @return 성공 여부
	 */
	@PostMapping("/user/request")
	public BaseResponse<Void> createRequest(@Login String userId, @RequestBody @Valid TaskRequestDto taskRequestDto) {
		taskRequestService.createRequest(taskRequestDto, userId);
		return BaseResponse.ok();
	}

	/**
	 * 주어진 연도와 월, 매니저 ID에 해당하는 월간 요청 데이터(처리상태별, 장비유형별, 업무유형별 요청건수)를 조회하여 반환한다.
	 *
	 * @param year 조회할 연도의 값 (예: 2024)
	 * @param month 조회할 월의 값 (1부터 12 사이의 값)
	 * @param user 요청 데이터를 조회할 매니저 ID
	 * @return 월간 요청 데이터(처리상태별, 장비유형별, 업무유형별 요청건수)
	 */
	@GetMapping("/request-manager/monthly-data")
	public BaseResponse<?> getMonthlyRequestData(@RequestParam("year") int year, @RequestParam("month") int month,@Login String user,@RequestParam("contractId") long contractId) {
		RequestManagerMainResponseDto requestManager = taskRequestService.getMonthlyRequestData(year, month, user, contractId);

		return BaseResponse.ok(requestManager);
	}

	/**
	 * 특정 연도와 월, 사용자, 계약 ID에 따라 요청 상태별 카운트를 조회하는 메서드.
	 *
	 * @param year       조회할 연도 (예: 2024).
	 * @param month      조회할 월 (1~12).
	 * @param user       현재 로그인한 사용자 ID. @Login 어노테이션을 통해 주입됨.
	 * @param contractId 조회할 계약의 ID.
	 * @return List<StatusCountDto> 형태로 상태별 카운트 리스트를 반환.
	 *
	 */
	@GetMapping("/common/request-status-count")
	public BaseResponse<?> getRequestStatus(@RequestParam("year") int year, @RequestParam("month") int month,@Login String user,@RequestParam("contractId") long contractId) {
		List<StatusCountDto> statusCounts = taskRequestService.getStatusCountByUser(year, month, user,contractId);

		return BaseResponse.ok(statusCounts);
	}

	/**
	 * 요청 내용 상세보기 메서드입니다.
	 *
	 * @param requestId 상세보기 할 요청 ID
	 * @return 상세 내용
	 */
	@GetMapping("/common/request/{requestId}")
	public BaseResponse<RequestDetailDto> showRequestDetail(@PathVariable("requestId") Long requestId) {
		RequestDetailDto requestDetailDto = taskRequestService.showRequestDetail(requestId);

		return BaseResponse.ok(requestDetailDto);

	}

	/**
	 * 요청 삭제 메서드입니다.
	 *
	 * @param requestId 삭제할 요청 ID
	 * @return 성공 여부
	 */
	@DeleteMapping("/request-manager/request/{requestId}")
	public BaseResponse<Void> deleteRequest(@Login String userId, @PathVariable("requestId") Long requestId) {
		taskRequestService.deleteRequest(requestId, userId);

		return BaseResponse.ok();
	}

	/**
	 * 요청 수정 메서드입니다.
	 *
	 * @param requestId 수정할 요청 ID
	 * @return 성공 여부
	 */
	@PatchMapping("/user/request/{requestId}")
	public BaseResponse<Void> editRequest(@Login String userId, @PathVariable("requestId") Long requestId,
		@RequestBody TaskRequestDto taskRequestDto) {
		taskRequestService.editRequest(requestId, userId, taskRequestDto);

		return BaseResponse.ok();
	}


	/**
	 * 계약관리자에서 할당 전 요청 매니저 전체 업무 현황 리스트 조회
	 *
	 * @return List<TaskRequestOfManagerDto>형태로
	 * 요청 매니저 전체 업무 현황 리스트를 포함한 응답 객체
	 */
	@GetMapping("/contract-manager/status")
	public BaseResponse<?> getManagerStatus() {
		List<TaskRequestOfManagerDto> taskRequestOfManager = taskRequestService.getTaskRequestOfManager();
		return BaseResponse.ok(taskRequestOfManager);
	}

	/**
	 * 필터링된 요청 목록을 조회하는 메서드입니다.
	 *
	 * @param equipmentName 필터링할 장비 이름 (옵션)
	 * @param type 필터링할 업무 유형 (옵션)
	 * @param taskDetail 필터링할 업무 세부 사항 (옵션)
	 * @param status 필터링할 요청 상태 (옵션)
	 * @param keyword 제목이나 내용에서 검색할 키워드 (옵션)
	 * @param page 조회할 페이지 번호 (기본값: 1)
	 * @param size 페이지당 항목 수 (기본값: 5)
	 * @return 요청 목록과 페이지네이션 정보를 포함한 응답 객체
	 */
	@GetMapping("/common/requests")
	public BaseResponse<?> getRequests(
		@RequestParam(required = false) String equipmentName,
		@RequestParam(required = false) String type,
		@RequestParam(required = false) String taskDetail,
		@RequestParam(required = false) RequestStatus status,
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "5") int size,
		@RequestParam(required = false) Integer year,
		@RequestParam(required = false) Integer month,
		@RequestParam("contractId") long contractId,
		@Login String user
	) {
		Pageable pageable = PageRequest.of(page - 1, size);

		// 서비스 메서드를 호출하여 RequestManagementResponseDto 객체를 받음
		RequestManagementResponseDto responseData = taskRequestService.findFilteredRequests(
			equipmentName, type, taskDetail, status, keyword, pageable, year, month, contractId, user
		);

		return BaseResponse.ok(responseData);
	}

	/**
	 * 요청을 특정 계약 관리자에게 할당하는 메서드.
	 *
	 * @param updateTaskRequestManagerDto 요청 할당 정보를 포함하는 DTO.
	 *                                    이 객체는 요청 ID와 요청 관리자 ID 등을 포함.
	 * @return {@link BaseResponse} 형태로 성공 여부를 반환.
	 */
	@PatchMapping("/contract-manager/request/allocate")
	public BaseResponse<Void> allocateRequest(@RequestBody UpdateTaskRequestManagerDto updateTaskRequestManagerDto) {
		taskRequestService.allocateRequest(updateTaskRequestManagerDto);
		return BaseResponse.ok();
	}


	/**
	 * 요청을 완료 상태로 변경하는 메서드.
	 *
	 * @param requestId 완료 처리할 요청의 ID.
	 * @param managerId 요청을 완료 처리하는 관리자의 ID.
	 *                  @Login 어노테이션을 통해 주입됨.
	 * @return BaseResponse 형태로 성공 여부를 반환.
	 */
	@PatchMapping("/request-manager/request/complete")
	public BaseResponse<Void> completeRequest(@RequestParam("requestId") long requestId,
		@Login String managerId) {
		taskRequestService.completeRequest(requestId, managerId);
		return BaseResponse.ok();
	}
}
