package project.slash.summary.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.summary.dto.SummaryDto;
import project.slash.summary.service.SummaryService;

@RestController
@RequiredArgsConstructor
public class SummaryController {
	private final SummaryService summaryService;

	@GetMapping("/common/statistics")
	public BaseResponse<?> getSummary(
		@RequestParam(value = "evaluationItemId", required = false) Long evaluationItemId,
		@RequestParam(value = "targetSystem", required = false) String targetSystem,
		@RequestParam(value = "targetEquipment", required = false) String targetEquipment,
		@RequestParam(value = "lastDate", required = false) String lastDate
	) {
		List<SummaryDto> statistics = summaryService.getSummary(evaluationItemId, targetSystem,
			targetEquipment, lastDate);

		return BaseResponse.ok(statistics);
	}

}
