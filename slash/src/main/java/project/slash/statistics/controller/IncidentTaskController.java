package project.slash.statistics.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.service.AutoStatisticsService;

@RestController
@RequiredArgsConstructor
public class IncidentTaskController {
	private final AutoStatisticsService autoStatisticsService;

	/**
	 * 지정된 날의 해당 월의 1일부터 지정된 날까지 장애 적기 처리율을 저장하는 메서드 입니다.
	 *
	 * @return success:true
	 */
	@PostMapping("/common/incident-statistics")
	public BaseResponse<Void> addIncidentDueOnTimeRate(
		@RequestBody @Valid RequestStatisticsDto requestStatisticsDto) {
		autoStatisticsService.getIncidentStatistics(requestStatisticsDto);
		return BaseResponse.ok();
	}
}
