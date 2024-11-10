package project.slash.statistics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.statistics.dto.response.MonthlyIndicatorsDto;
import project.slash.statistics.dto.response.StatisticsStatusDto;
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
	public BaseResponse<?> getMonthlyIndicators(@PathVariable Long contractId, @PathVariable int year, @PathVariable int month) {
		MonthlyIndicatorsDto monthlyIndicators = statisticsService.getMonthlyIndicators(contractId, year, month);

		return BaseResponse.ok(monthlyIndicators);
	}

	/**
	 * 계산, 미계산 통계 조회 메서드입니다.
	 *
	 * @param contractId 조회할 계약 아이디
	 * @param year 조회할 년도
	 * @param month 조회할 달
	 * @return 계산, 미계산 통계 지표 리스트
	 */
	@GetMapping("/contract-manager/statistics/status/{contractId}/{year}/{month}/{day}")
	public BaseResponse<StatisticsStatusDto> getStatisticsStatus(@PathVariable Long contractId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
		StatisticsStatusDto statisticsStatus = statisticsService.getStatisticsStatus(contractId, year, month, day);

		return BaseResponse.ok(statisticsStatus);
	}

	/**
	 * 계산된 통계 승인하는 메서드입니다.
	 *
	 * @param statisticsId 승인할 통계 아이디
	 * @param evaluationItemId 승인할 평가 항목 아이디
	 * @return 성공 여부
	 */
	@PatchMapping("/contract-manager/{statisticsId}/approve/{evaluationItemId}")
	public BaseResponse<Void> approve(@PathVariable Long statisticsId, @PathVariable Long evaluationItemId) {
		statisticsService.approve(statisticsId, evaluationItemId);

		return BaseResponse.ok();
	}
}
