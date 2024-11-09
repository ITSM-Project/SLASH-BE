package project.slash.statistics.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.service.IncidentStatisticsService;

@RestController
@RequiredArgsConstructor
public class IncidentTaskController {
	private final IncidentStatisticsService incidentStatisticsService;

	@PostMapping("/common/statistics/incident-statistics")
	public BaseResponse<Void> addIncidentDueOnTimeRate(
		@RequestBody @Valid RequestStatisticsDto requestStatisticsDto) {
		incidentStatisticsService.getIncidentStatistics(requestStatisticsDto);
		return BaseResponse.ok();
	}
}
