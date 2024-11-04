package project.slash.statistics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.StatsDto;
import project.slash.statistics.service.StatisticsService;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
	private final StatisticsService statisticsService;

	@GetMapping("/statistics")
	public BaseResponse<?> getStatistics(@RequestParam("serviceType") String serviceType,@RequestParam("period") String period,@RequestParam("targetSystem") String targetSystem) {
		List<StatsDto> statistics = statisticsService.getStatistics(serviceType, period, targetSystem);

		return BaseResponse.ok(statistics);
	}

}
