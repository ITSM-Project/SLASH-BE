package project.slash.statistics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.statistics.service.StatisticsService;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
	private final StatisticsService statisticsService;

	@GetMapping("/common/statistics")
	public BaseResponse<?> getStatistics(@RequestParam(value = "serviceType", required = false) String serviceType,
		@RequestParam(value = "period", required = false) String period,
		@RequestParam(value = "targetSystem", required = false) String targetSystem,
		@RequestParam(value = "targetEquipment", required = false) String targetEquipment){
		List<StatisticsDto> statistics = statisticsService.getStatistics(serviceType, period, targetSystem,targetEquipment);

		return BaseResponse.ok(statistics);
	}

}
