package project.slash.statistics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.statistics.dto.response.MonthlyIndicatorsDto;
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

	/**
	 * 월간 지표 조회하는 메서드입니다.
	 *
	 * @param contractId 지표 조회할 계약서 아이디
	 * @param year 조회할 년도
	 * @param month 조회할 월
	 * @return 월간 지표
	 */
	@GetMapping("/common/{contractId}/indicators/{year}/{month}")
	public BaseResponse<?> getMonthlyIndicators(@PathVariable("contractId") Long contractId,
		@PathVariable("year") int year,
		@PathVariable("month") int month) {
		MonthlyIndicatorsDto monthlyIndicators = statisticsService.getMonthlyIndicators(contractId, year, month);

		return BaseResponse.ok(monthlyIndicators);
	}
}
