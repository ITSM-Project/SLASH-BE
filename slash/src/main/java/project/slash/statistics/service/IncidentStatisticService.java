package project.slash.statistics.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.ContractDataDto;
import project.slash.contract.repository.ContractRepository;
import project.slash.statistics.dto.MonthlyIncidentDataDto;
import project.slash.statistics.dto.MonthlyServiceStatisticsDto;
import project.slash.statistics.repository.StatisticsRepository;

@Service
@RequiredArgsConstructor
public class IncidentStatisticService {
	private final StatisticsRepository statisticsRepository;
	private final ContractRepository contractRepository;

	public void createMonthlyStats(String serviceType) {
		List<MonthlyServiceStatisticsDto> monthlyServiceStatisticsDtoList = calculateMonthlyStats(serviceType);
		statisticsRepository.saveMonthlyData(monthlyServiceStatisticsDtoList);
	}

	// 자동 계산 로직, 조건별 산출식 변경
	public List<MonthlyServiceStatisticsDto> calculateMonthlyStats(String serviceType) {
		List<MonthlyIncidentDataDto> monthlyData = statisticsRepository.getMonthlyIncidentData();
		List<ContractDataDto> contractData = contractRepository.findIndicatorByCategory(serviceType);
		List<MonthlyServiceStatisticsDto> result = new ArrayList<>();

		for (MonthlyIncidentDataDto monthlyDataDto : monthlyData) {
			double score = 0.0;
			double scaled_score = 0.0;
			// 총 장애 건수 - 적기 처리 건수 = 지연 처리 건수
			score = monthlyDataDto.getSystemIncidentCount() - monthlyDataDto.getDueOnTimeCount();
			String grade = null;
			double weightedScore = 0.0;
			long EvaluationItemId = 0L;
			for (ContractDataDto contractDataDto : contractData) {
				Boolean isInTargetRange = isInTargetRange(
					contractDataDto.getMax(),
					contractDataDto.isMaxInclusive(),
					contractDataDto.getMin(),
					contractDataDto.isMinInclusive(),
					score
				);
				if (isInTargetRange) {
					grade = contractDataDto.getGrade();
					scaled_score = contractDataDto.getScore();
					EvaluationItemId = contractDataDto.getEvaluationItemId();
					weightedScore = getWeightedScore(contractDataDto.getWeight(), contractDataDto.getWeightTotal(),
						scaled_score);

					break;
				}
			}
			String yearMonthString = monthlyDataDto.getYearMonth();
			LocalDate date = LocalDate.parse(yearMonthString + "-" + monthlyDataDto.getLastDay(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			result.add(
				new MonthlyServiceStatisticsDto(date, serviceType, monthlyDataDto.getEquipmentName(), grade,
					scaled_score,
					"월별", weightedScore, true,
					monthlyDataDto.getTotalDownTime(), monthlyDataDto.getRequestCount(), EvaluationItemId,
					monthlyDataDto.getSystemName(), scaled_score, monthlyDataDto.getSystemIncidentCount(),
					monthlyDataDto.getDueOnTimeCount()));

		}

		return result;
	}

	private double getWeightedScore(int weight, int weightTotal, double score) {
		double ratio = (double)weight / weightTotal;
		double weightedScore = score * ratio;

		return Math.round(weightedScore * 100.0) / 100.0;
	}

	private Boolean isInTargetRange(double max, boolean maxInclusive, double min, boolean minInclusive,
		double score) {
		boolean minCondition = minInclusive ? score >= min : score > min;
		boolean maxCondition = maxInclusive ? score <= max : score < max;
		return minCondition && maxCondition;
	}

}
