package project.slash.taskrequest.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.response.RequestManagementResponseDto;
import project.slash.taskrequest.dto.response.RequestManagerMainResponseDto;
import project.slash.taskrequest.dto.response.RequestDetailDto;
import project.slash.taskrequest.model.constant.RequestStatus;
import project.slash.taskrequest.service.TaskRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/request-manager")
public class TaskRequestController {
	private final TaskRequestService taskRequestService;

	/**
	 * 요청 생성 메서드입니다.
	 *
	 * @param taskRequestDto 요청 정보
	 * @return 성공 여부
	 */
	@PostMapping("/request")
	public BaseResponse<Void> createRequest(@RequestBody @Valid TaskRequestDto taskRequestDto) {
		taskRequestService.createRequest(taskRequestDto);

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
	@GetMapping("/monthly-data")
	public BaseResponse<?> getMonthlyRequestData(@RequestParam("year") int year, @RequestParam("month") int month, String user) {
		RequestManagerMainResponseDto requestManager = taskRequestService.getMonthlyRequestData(year, month, "2");

		return BaseResponse.ok(requestManager);
	}

	/**
	 * 요청 내용 상세보기 메서드입니다.
	 *
	 * @param requestId 상세보기 할 요청 ID
	 * @return 상세 내용
	 */
	@GetMapping("/request/{requestId}")
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
	@DeleteMapping("/request/{requestId}")
	public BaseResponse<Void> deleteRequest(@PathVariable("requestId") Long requestId) {
		taskRequestService.deleteRequest(requestId, "1");    //TODO: 로그인 된 사용자로 변경해야함

		return BaseResponse.ok();
	}

	/**
	 * 요청 수정 메서드입니다.
	 *
	 * @param requestId 수정할 요청 ID
	 * @return 성공 여부
	 */
	@PatchMapping("/request/{requestId}")
	public BaseResponse<Void> editRequest(@PathVariable("requestId") Long requestId,
		@RequestBody TaskRequestDto taskRequestDto) {
		taskRequestService.editRequest(requestId, "1", taskRequestDto);

		return BaseResponse.ok();
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
	@GetMapping("/requests")
	public BaseResponse<?> getRequests(
		@RequestParam(required = false) String equipmentName,
		@RequestParam(required = false) String type,
		@RequestParam(required = false) String taskDetail,
		@RequestParam(required = false) RequestStatus status,
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "5") int size
	) {
		Pageable pageable = PageRequest.of(page - 1, size);

		// 서비스 메서드를 호출하여 RequestManagementResponseDto 객체를 받음
		RequestManagementResponseDto responseData = taskRequestService.findFilteredRequests(
			equipmentName, type, taskDetail, status, keyword, pageable
		);


		return BaseResponse.ok(responseData);
	}


}
