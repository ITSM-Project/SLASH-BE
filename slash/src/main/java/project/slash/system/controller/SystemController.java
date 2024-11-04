package project.slash.system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.system.dto.response.AllSystemsInfo;
import project.slash.system.service.SystemService;

@RestController
@RequiredArgsConstructor
public class SystemController {
	private final SystemService systemService;

	/**
	 * 저장된 모든 시스템-장비 조회하는 메서드 입니다.
	 *
	 * @return 시스템-장비 정보
	 */
	@GetMapping("/all-systems")
	public BaseResponse<?> showAllSystems(){
		List<AllSystemsInfo> allSystemsInfos = systemService.showAllSystems();

		return BaseResponse.ok(allSystemsInfos);
	}

	/**
	 * 시스템의 고유한 이름 목록을 조회하는 메서드입니다.
	 *
	 * @return 시스템 이름 목록
	 */
	@GetMapping("/systems")
	public BaseResponse<List<String>> getDistinctSystemNames() {
		List<String> allSystemsNames = systemService.getDistinctSystemNames();
		return BaseResponse.ok(allSystemsNames);
	}
}
