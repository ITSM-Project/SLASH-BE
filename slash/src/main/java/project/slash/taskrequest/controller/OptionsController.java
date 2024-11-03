package project.slash.taskrequest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.taskrequest.service.OptionsService;

@RestController
@RequestMapping("/options")
@RequiredArgsConstructor
public class OptionsController {

	private final OptionsService optionsService;

	/**
	 * 시스템의 고유한 이름 목록을 조회하는 메서드입니다.
	 *
	 * @return 시스템 이름 목록
	 */
	@GetMapping("/systems")
	public List<String> getDistinctSystemNames() {
		return optionsService.getDistinctSystemNames();
	}

	/**
	 * 현재 계약의 업무 유형 목록을 조회하는 메서드입니다.
	 *
	 * @return 업무 유형 목록
	 */
	@GetMapping("/task-type")
	public List<String> getDistinctTaskType() {
		return optionsService.getDistinctTaskTypes();
	}

	/**
	 * 현재 계약의 업무 세부 사항 목록을 조회하는 메서드입니다.
	 *
	 * @return 업무 세부 사항 목록
	 */
	@GetMapping("/task-detail")
	public List<String> getDistinctTaskDetails() {
		return optionsService.getDistinctTaskDetails();
	}

}
