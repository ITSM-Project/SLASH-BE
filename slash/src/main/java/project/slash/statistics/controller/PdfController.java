package project.slash.statistics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.service.EvaluationItemService;
import project.slash.statistics.dto.response.MonthlyServiceStatisticsDto;
import project.slash.statistics.service.PdfService;
import project.slash.statistics.service.StatisticsService;

@Controller
@RequiredArgsConstructor
public class PdfController {
	private final StatisticsService statisticsService;
	private final EvaluationItemService evaluationItemService;
	private final PdfService pdfService;

	@GetMapping("/common/pdf/{id}")
	public void generateStatisticsPdf(@PathVariable("id") Long evaluationItemId,
		@RequestParam("date") LocalDate calculateTime,
		HttpServletResponse response) throws Exception {
		List<MonthlyServiceStatisticsDto> statistics = statisticsService.getStatistics(evaluationItemId, calculateTime);
		EvaluationItemDetailDto evaluationItem = evaluationItemService.findDetailByItemId(evaluationItemId);

		pdfService.generateStatisticsPdf(statistics, evaluationItem, response);
	}
}
