package project.slash.statistics.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.dto.response.ResponseStatisticsDto;
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
	@PostMapping("/contract-manager/incident-statistics")
	public BaseResponse<Void> addIncidentDueOnTimeRate(
		@RequestBody @Valid RequestStatisticsDto requestStatisticsDto) {
		autoStatisticsService.addIncidentStatistics(requestStatisticsDto);
		return BaseResponse.ok();
	}

	/**
	 * 지정된 날의 해당 월의 1일부터 지정된 날까지 장애 적기 처리율을 불러오는 메서드 입니다.
	 *
	 * @return success:true, data:ResponseStatisticsDto
	 */
	@GetMapping("/common/incident-statistics")
	public BaseResponse<ResponseStatisticsDto> getIncidentDueOnTimeRate(
		@RequestParam("evaluationItemId") Long evaluationItemId,
		@RequestParam("date") LocalDate date) {
		ResponseStatisticsDto responseStatisticsDto = autoStatisticsService.getIncidentStatistics(evaluationItemId,
			date);

		return BaseResponse.ok(responseStatisticsDto);
	}
}
