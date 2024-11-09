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
import project.slash.statistics.dto.request.SelectedDateDto;
import project.slash.statistics.dto.response.MonthlyStatisticsDto;
import project.slash.statistics.service.StatisticsService;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
	private final StatisticsService statisticsService;

	@GetMapping("/common/statistics")
	public BaseResponse<?> getStatistics(
		@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		@RequestParam("evaluationItemId") long evaluationItemId) {
		List<MonthlyStatisticsDto> statisticsList = statisticsService.calculateMonthlyStats(date, evaluationItemId);
		return BaseResponse.ok(statisticsList);
	}

	@PostMapping("/contract-manager/statistics")
	public BaseResponse<?> addStatistics(@RequestBody SelectedDateDto selectedDateDto) {
		statisticsService.createMonthlyStats(selectedDateDto.getDate(),
			selectedDateDto.getEvaluationItemId());

		return BaseResponse.ok();
	}

}
