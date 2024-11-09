package project.slash.statistics.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.service.StatisticsService;

@RestController
@RequiredArgsConstructor
public class ServiceStaticsController {

	private final StatisticsService statisticsService;

	/**
	 * @param requestStatisticsDto 통계낼 evaluationId와 Date 요청정보
	 * @return 저장성공여부
	 */
	@PostMapping("/contract-manager/service-statistic")
	public BaseResponse<Void> createServiceStatics(
		@RequestBody @Valid RequestStatisticsDto requestStatisticsDto) {
		statisticsService.createServiceTaskStatics(requestStatisticsDto);
		return BaseResponse.ok();
	}
}
