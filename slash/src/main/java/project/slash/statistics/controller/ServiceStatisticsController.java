package project.slash.statistics.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.dto.response.ResponseStatisticsDto;
import project.slash.statistics.service.StatisticsService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ServiceStatisticsController {

	private final StatisticsService statisticsService;

	/**
	 * @param requestStatisticsDto 통계낼 evaluationId와 Date 요청정보
	 * @return 저장성공여부
	 */
	@PostMapping("/contract-manager/service-statistic")
	public BaseResponse<Void> createServiceStatics(
		@RequestBody @Valid RequestStatisticsDto requestStatisticsDto) {
		statisticsService.createServiceTaskStatistics(requestStatisticsDto);
		return BaseResponse.ok();
	}

	/**
	 *
	 * @param evaluationId 조회할 아이디
	 * @param date 조회 날짜
	 * @return 해당시점 통계 정보
	 */
	@GetMapping("/common/service-statistic")
	public BaseResponse<ResponseStatisticsDto> getServiceStatics(@RequestParam("evaluationId") Long evaluationId,
		@RequestParam("date")
		LocalDate date) {
		ResponseStatisticsDto responseStatisticsDto = statisticsService.getServiceStatistics(evaluationId, date);
		System.out.println(responseStatisticsDto);
		log.info(responseStatisticsDto.toString());
		return BaseResponse.ok(responseStatisticsDto);
	}
}
