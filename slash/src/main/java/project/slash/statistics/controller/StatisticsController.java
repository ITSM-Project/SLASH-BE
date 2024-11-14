package project.slash.statistics.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.response.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.request.EditStatisticsDto;
import project.slash.statistics.dto.response.MonthlyIndicatorsDto;
import project.slash.statistics.dto.response.StatisticsStatusDto;
import project.slash.statistics.service.StatisticsService;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
	private final StatisticsService statisticsService;

	/**
	 * 월간 지표 조회하는 메서드입니다.
	 *
	 * @param contractId 지표 조회할 계약서 아이디
	 * @param date 조회할 날짜
	 * @return 월간 지표
	 */
	@GetMapping("/common/{contractId}/indicators")
	public BaseResponse<MonthlyIndicatorsDto> getMonthlyIndicators(@PathVariable Long contractId, @RequestParam YearMonth date) {
		MonthlyIndicatorsDto monthlyIndicators = statisticsService.getMonthlyIndicators(contractId, date);

		return BaseResponse.ok(monthlyIndicators);
	}

	/**
	 * 계산, 미계산 통계 조회 메서드입니다.
	 *
	 * @param contractId 조회할 계약 아이디
	 * @param date 조회할 날짜
	 * @return 계산, 미계산 통계 지표 리스트
	 */
	@GetMapping("/contract-manager/statistics/status/{contractId}")
	public BaseResponse<StatisticsStatusDto> getStatisticsStatus(@PathVariable Long contractId, @RequestParam YearMonth date) {
		StatisticsStatusDto statisticsStatus = statisticsService.getStatisticsStatus(contractId, date);

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

	/**
	 * 통계 결과 수정하는 메서드입니다.
	 *
	 * @param statisticsId 수정할 통계 아이디
	 * @return 성공 여부
	 */
	@PatchMapping("/contract-manager/statistics/{id}")
	public BaseResponse<Void> editStatistics(@PathVariable("id") Long statisticsId, @RequestBody EditStatisticsDto editStatisticsDto) {
		statisticsService.editStatistics(statisticsId, editStatisticsDto);

		return BaseResponse.ok();
	}

	/**
	 * 지표 결과 조회하는 메서드입니다. (상세보기)
	 *
	 * @param evaluationItemId 조회할 평가 항목 아이디
	 * @param date 계산된 시간
	 * @return 확정된 지표 결과
	 */
	@GetMapping("/common/statistics/evaluation-item/{id}")
	public BaseResponse<List<MonthlyServiceStatisticsDto>> getStatistics(@PathVariable("id") Long evaluationItemId,
		@RequestParam("date") LocalDate date) {
		List<MonthlyServiceStatisticsDto> statistics = statisticsService.getStatistics(evaluationItemId, date);

		return BaseResponse.ok(statistics);
	}

	/**
	 * 계산된 통계 삭제하는 메서드입니다.
	 *
	 * @param evaluationItemId 삭제할 항목
	 * @param date 계산된 시간
	 * @return 성공 여부
	 */
	@DeleteMapping("/contract-manager/statistics/{id}")
	public BaseResponse<Void> deleteCalculateStatistics(@PathVariable("id") Long evaluationItemId,
		@RequestParam("date") LocalDate date) {
		statisticsService.deleteStatistics(evaluationItemId, date);

		return BaseResponse.ok();
	}
}
