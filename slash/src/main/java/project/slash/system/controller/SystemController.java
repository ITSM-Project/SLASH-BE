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

	@GetMapping("/all-systems")
	public BaseResponse<?> showAllSystems(){
		List<AllSystemsInfo> allSystemsInfos = systemService.showAllSystems();

		return BaseResponse.ok(allSystemsInfos);
	}
}
