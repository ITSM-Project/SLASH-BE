package project.slash.taskrequest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.taskrequest.dto.request.TaskRequestDto;
import project.slash.taskrequest.dto.response.TaskTypeDto;
import project.slash.taskrequest.service.TaskRequestService;

@RestController
@RequiredArgsConstructor
public class TaskRequestController {
	private final TaskRequestService taskRequestService;

	@GetMapping("/all-task-types")
	public BaseResponse<?> allTaskTypes() {
		List<TaskTypeDto> allTaskTypes = taskRequestService.allTaskTypes();

		return BaseResponse.ok(allTaskTypes);
	}

	@PostMapping("/request")
	public BaseResponse<Void> createRequest(@RequestBody TaskRequestDto taskRequestDto) {
		taskRequestService.createRequest(taskRequestDto);

		return BaseResponse.ok();
	}
}
