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

	/**
	 * 특정 날짜와 평가 항목 ID를 기반으로 서비스 가동률 통계를 조회하는 메서드.
	 *
	 * @param date              통계를 계산할 기준 날짜. {@link LocalDate} 형식.
	 * @param evaluationItemId  통계를 계산할 평가 항목의 ID.
	 * @return BaseResponse 형태로 월별 서비스 가동률 통계 리스트를 반환.
	 *         반환 데이터는 List<ResponseStatisticsDto>형태로 제공됨.
	 *
	 */
	@GetMapping("/common/statistics")
	public BaseResponse<?> getServiceUptimeStatistics(
		@RequestParam("date") LocalDate date,
		@RequestParam("evaluationItemId") long evaluationItemId) {
		List<ResponseStatisticsDto> statisticsList = autoStatisticsService.calculateMonthlyStats(date,
			evaluationItemId);
		return BaseResponse.ok(statisticsList);
	}

	/**
	 * 월간 서비스 가동률 통계를 추가하는 메서드.
	 *
	 * @param requestStatisticsDto 추가할 통계 정보를 포함하는 DTO.
	 *                             통계 계산에 필요한 데이터(evaluationItemId,date)가 포함되어 있음.
	 * @return BaseResponse 형태로 성공 여부를 반환.
	 *
	 */
	@PostMapping("/contract-manager/statistics")
	public BaseResponse<?> addStatistics(@RequestBody RequestStatisticsDto requestStatisticsDto) {
		autoStatisticsService.createMonthlyUptimeStatistics(requestStatisticsDto);

		return BaseResponse.ok();
	}
}
