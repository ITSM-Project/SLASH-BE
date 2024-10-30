package project.slash.taskrequest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
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
import project.slash.taskrequest.dto.response.AllTaskTypeDto;
import project.slash.taskrequest.dto.response.RequestDetailDto;
import project.slash.taskrequest.service.TaskRequestService;

@RestController
@RequiredArgsConstructor
public class TaskRequestController {
	private final TaskRequestService taskRequestService;

	/**
	 * 현재 계약의 업무 유형 조회 메서드 입니다.
	 *
	 * @return 업무 유형
	 */
	@GetMapping("/all-task-types")
	public BaseResponse<List<AllTaskTypeDto>> allTaskTypes() {
		List<AllTaskTypeDto> allTaskTypes = taskRequestService.allTaskTypes();

		return BaseResponse.ok(allTaskTypes);
	}

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

	@GetMapping("/monthly-data")
	public BaseResponse<?> getMonthlyRequestData(@RequestParam("year") int year, @RequestParam("month") int month,
		String user) {
		RequestManagerMainResponseDto requestManager = taskRequestService.getMonthlyRequestData(year, month, "1");
		return BaseResponse.ok(requestManager);
	}
}