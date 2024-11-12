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
public class ServiceStatisticsController {

	private final AutoStatisticsService autoStatisticsService;

	/**
	 * 서비스 적기 처리율 통계 생성 메서드입니다.
	 *
	 * @param requestStatisticsDto 통계낼 evaluationId와 Date 요청정보
	 * @return 저장성공여부
	 */
	@PostMapping("/contract-manager/service-statistic")
	public BaseResponse<Void> createServiceStatics(
		@RequestBody @Valid RequestStatisticsDto requestStatisticsDto) {
		autoStatisticsService.createServiceTaskStatistics(requestStatisticsDto);
		return BaseResponse.ok();
	}

	/**
	 * 서비스 적기 처리율 통계 조회 메서드 입니다.
	 *
	 * @param evaluationId 조회할 아이디
	 * @param date 조회 날짜
	 * @return 해당시점 통계 정보
	 */
	@GetMapping("/common/service-statistic")
	public BaseResponse<ResponseStatisticsDto> getServiceStatics(@RequestParam("evaluationId") Long evaluationId,
		@RequestParam("date")
		LocalDate date) {
		ResponseStatisticsDto responseStatisticsDto = autoStatisticsService.getServiceStatistics(evaluationId, date);
		return BaseResponse.ok(responseStatisticsDto);
	}
}
