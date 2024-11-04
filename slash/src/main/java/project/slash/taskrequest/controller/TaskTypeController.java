package project.slash.taskrequest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.taskrequest.dto.response.AllTaskTypeDto;
import project.slash.taskrequest.service.TaskTypeService;

@RestController
@RequiredArgsConstructor
// @RequestMapping("/request-manager")
public class TaskTypeController {
	private final TaskTypeService taskTypeService;

	/**
	 * 현재 계약의 업무 유형 조회 메서드 입니다.
	 *
	 * @return 업무 유형
	 */
	@GetMapping("/all-task-types")
	public BaseResponse<List<AllTaskTypeDto>> allTaskTypes() {
		List<AllTaskTypeDto> allTaskTypes = taskTypeService.allTaskTypes();

		return BaseResponse.ok(allTaskTypes);
	}

	/**
	 * 현재 계약의 업무 유형 목록을 조회하는 메서드입니다.
	 *
	 * @return 업무 유형 목록
	 */

	@GetMapping("/task-type")
	public BaseResponse<List<String>> getDistinctTaskType() {
		List<String> allTaskTypes = taskTypeService.getDistinctTaskTypes();
		return BaseResponse.ok(allTaskTypes);
	}

	/**
	 * 현재 계약의 업무 세부 사항 목록을 조회하는 메서드입니다.
	 *
	 * @return 업무 세부 사항 목록
	 */
	@GetMapping("/task-detail")
	public BaseResponse<List<String>> getDistinctTaskDetails() {
		List<String> allTaskDetails = taskTypeService.getDistinctTaskDetails();
		return BaseResponse.ok(allTaskDetails);
	}
}
