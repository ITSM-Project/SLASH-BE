package project.slash.taskrequest.controller;

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
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.response.RequestManagerMainResponseDto;
import project.slash.taskrequest.dto.response.RequestDetailDto;
import project.slash.taskrequest.service.TaskRequestService;

@RestController
@RequiredArgsConstructor
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

	@GetMapping("/monthly-data")
	public BaseResponse<?> getMonthlyRequestData(@RequestParam("year") int year, @RequestParam("month") int month, String user) {
		RequestManagerMainResponseDto requestManager = taskRequestService.getMonthlyRequestData(year, month, "1");

		return BaseResponse.ok(requestManager);
	}

	/**
	 * 요청 내용 상세보기 메서드입니다.
	 *
	 * @param requestId 상세보기 할 요청ID
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
		taskRequestService.deleteRequest(requestId, "1");	//TODO: 로그인 된 사용자로 변경해야함

		return BaseResponse.ok();
	}

	/**
	 * 요청 수정 메서드입니다.
	 *
	 * @param requestId 수정할 요청 ID
	 * @return 성공 여부
	 */
	@PatchMapping("/request/{requestId}")
	public BaseResponse<Void> editRequest(@PathVariable("requestId") Long requestId, @RequestBody TaskRequestDto taskRequestDto) {
		taskRequestService.editRequest(requestId, "1", taskRequestDto);

		return BaseResponse.ok();
	}
}
