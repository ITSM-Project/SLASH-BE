package project.slash.statistics.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.dto.response.ResponseStatisticsDto;
import project.slash.statistics.service.AutoStatisticsService;

@RequiredArgsConstructor
@RestController
public class ServiceUptimeController {
	private final AutoStatisticsService autoStatisticsService;

	@GetMapping("/common/statistics")
	public BaseResponse<?> getServiceUptimeStatistics(
		@RequestParam("date") LocalDate date,
		@RequestParam("evaluationItemId") long evaluationItemId) {
		List<ResponseStatisticsDto> statisticsList = autoStatisticsService.calculateMonthlyStats(date, evaluationItemId);
		return BaseResponse.ok(statisticsList);
	}

	@PostMapping("/contract-manager/statistics")
	public BaseResponse<?> addStatistics(@RequestBody RequestStatisticsDto requestStatisticsDto) {
		autoStatisticsService.createMonthlyUptimeStatistics(requestStatisticsDto);

		return BaseResponse.ok();
	}
}
