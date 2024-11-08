package project.slash.taskrequest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.taskrequest.dto.response.AllTaskTypeDto;
import project.slash.taskrequest.service.TaskTypeService;

@RestController
@RequiredArgsConstructor
public class TaskTypeController {
	private final TaskTypeService taskTypeService;
	
	/**
	 * 현재 계약의 업무 유형 조회 메서드 입니다.
	 * 
	 * @param contractId 계약 아이디
	 * @return 현재 계약 업무 유형
	 */
	@GetMapping("/user/all-task-types/{contractId}")
	public BaseResponse<List<AllTaskTypeDto>> allTaskTypes(@PathVariable("contractId") Long contractId) {
		List<AllTaskTypeDto> allTaskTypes = taskTypeService.allTaskTypes(contractId);

		return BaseResponse.ok(allTaskTypes);
	}

	/**
	 * 현재 계약의 업무 유형 목록을 조회하는 메서드입니다.
	 *
	 * @return 업무 유형 목록
	 */
	@GetMapping("/common/task-type")
	public BaseResponse<List<String>> getDistinctTaskType() {
		List<String> allTaskTypes = taskTypeService.getDistinctTaskTypes();
		return BaseResponse.ok(allTaskTypes);
	}

	/**
	 * 현재 계약의 업무 세부 사항 목록을 조회하는 메서드입니다.
	 *
	 * @return 업무 세부 사항 목록
	 */
	@GetMapping("/common/task-detail")
	public BaseResponse<List<String>> getDistinctTaskDetails() {
		List<String> allTaskDetails = taskTypeService.getDistinctTaskDetails();
		return BaseResponse.ok(allTaskDetails);
	}
}
