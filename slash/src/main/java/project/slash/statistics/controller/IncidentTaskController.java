package project.slash.statistics.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.service.IncidentStatisticsService;

@RestController
public class IncidentTaskController {

	@PostMapping("/contract-manager/statistics")
	public BaseResponse<Void> addIncidentResolvedOnTimeRate(
		@RequestBody @Valid RequestStatisticsDto requestStatisticsDto) {
		IncidentStatisticsService.saveIncidentStatistics(requestStatisticsDto.getEvaluationItemId(),
			requestStatisticsDto.getEndDate());
		return BaseResponse.ok();
	}
}
