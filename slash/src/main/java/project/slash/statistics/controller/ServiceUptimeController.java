package project.slash.statistics.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.dto.response.MonthlyStatisticsDto;
import project.slash.statistics.service.AutoStatisticsService;

@RequiredArgsConstructor
@RestController
public class ServiceUptimeController {
	private final AutoStatisticsService autoStatisticsService;

	@GetMapping("/common/statistics")
	public BaseResponse<?> getServiceUptimeStatistics(
		@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
		@RequestParam("evaluationItemId") long evaluationItemId) {
		List<MonthlyStatisticsDto> statisticsList = autoStatisticsService.calculateMonthlyStats(date, evaluationItemId);
		return BaseResponse.ok(statisticsList);
	}

	@PostMapping("/contract-manager/statistics")
	public BaseResponse<?> addStatistics(@RequestBody RequestStatisticsDto requestStatisticsDto) {
		autoStatisticsService.createMonthlyStats(requestStatisticsDto);

		return BaseResponse.ok();
	}
}
