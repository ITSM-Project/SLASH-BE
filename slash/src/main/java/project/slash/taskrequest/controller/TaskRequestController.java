package project.slash.taskrequest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.taskrequest.dto.request.CreateTaskTypeDto;
import project.slash.taskrequest.service.TaskRequestService;

@RestController
@RequiredArgsConstructor
public class TaskRequestController {
	private final TaskRequestService taskRequestService;

	@PostMapping("/task-type")
	public BaseResponse<Void> createTaskType(@RequestBody List<CreateTaskTypeDto> createTaskTypes) {
		taskRequestService.createTaskType(createTaskTypes);

		return BaseResponse.ok();
	}
}
