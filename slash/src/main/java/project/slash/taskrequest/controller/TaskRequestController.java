package project.slash.taskrequest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.request.TaskResponseRequestDTO;
import project.slash.taskrequest.dto.response.AllTaskTypeDto;
import project.slash.taskrequest.model.constant.RequestStatus;
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
	public BaseResponse<?> allTaskTypes() {
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
	public BaseResponse<Void> createRequest(@RequestBody TaskRequestDto taskRequestDto) {
		taskRequestService.createRequest(taskRequestDto);

		return BaseResponse.ok();
	}

	@GetMapping("/requests")
	public BaseResponse<?> readRequest(
		@RequestParam(required = false) String equipmentName,
		@RequestParam(required = false) String type,
		@RequestParam(required = false) String taskDetail,
		@RequestParam(required = false) RequestStatus status,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "4") int size
	) {
		// Pageable 객체 생성
		Pageable pageable = PageRequest.of(page - 1, size);

		// Page 객체 반환
		Page<TaskResponseRequestDTO> taskResponseRequestDTOS =
			taskRequestService.findFilteredRequests(equipmentName, type, taskDetail, status, pageable);

		// 응답 데이터로 페이지네이션 정보를 포함하여 반환
		Map<String, Object> responseData = Map.of(
			"results", taskResponseRequestDTOS.getContent(),
			"totalPages", taskResponseRequestDTOS.getTotalPages(),
			"currentPage", taskResponseRequestDTOS.getNumber() + 1, // 현재 페이지
			"totalItems", taskResponseRequestDTOS.getTotalElements() // 총 항목 수
		);

		return BaseResponse.ok(responseData);
	}

}
