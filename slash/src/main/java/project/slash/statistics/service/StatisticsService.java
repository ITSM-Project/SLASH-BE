package project.slash.statistics.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.ContractDataDto;
import project.slash.contract.repository.ContractRepository;
import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatsDto;
import project.slash.statistics.dto.StatsDto;
import project.slash.statistics.repository.StatisticsRepository;

@Service
@RequiredArgsConstructor
public class StatisticsService {
	private final StatisticsRepository statisticsRepository;
	private final ContractRepository contractRepository;

	public void createMonthlyStats(String serviceType) {
		List<MonthlyServiceStatsDto> monthlyServiceStatsDtoList = calculateMonthlyStats(serviceType);
		statisticsRepository.saveMonthlyData(monthlyServiceStatsDtoList);
	}

	// 자동 계산 로직, 조건별 산출식 변경
	public List<MonthlyServiceStatsDto> calculateMonthlyStats(String serviceType) {
		List<MonthlyDataDto> monthlyData = statisticsRepository.getMonthlyData();
		List<ContractDataDto> contractData = contractRepository.findIndicatorByCategory(serviceType);

		List<MonthlyServiceStatsDto> result = new ArrayList<>();

		for (MonthlyDataDto monthlyDataDto : monthlyData) {
			double score = 0.0;
			//이부분은 서비스타입별로 다륾
			if (serviceType.equals("서비스 가동률")) {
				score = getServiceUptimeScore(monthlyDataDto.getLastDay(), monthlyDataDto.getTotalDownTime());
			}

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
					EvaluationItemId = contractDataDto.getEvaluationItemId();
					weightedScore = getWeightedScore(contractDataDto.getWeight(), contractDataDto.getWeightTotal(),
						score);
					break;
				}
			}
			String yearMonthString = monthlyDataDto.getYearMonth();
			LocalDate date = LocalDate.parse(yearMonthString + "-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			//이부분은 서비스 타입 별로 다륾
			// 서비스 가동률의 경우 , 아래에 if문 추가하면 됨
			if (serviceType.equals("서비스 가동률")) {
				result.add(new MonthlyServiceStatsDto(date, serviceType, grade, score, "월별", weightedScore, true,
					monthlyDataDto.getTotalDownTime(), monthlyDataDto.getRequestCount(), EvaluationItemId,
					monthlyDataDto.getSystemName(), score, monthlyDataDto.getSystemIncidentCount(), 0L));
			}

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

	private double getServiceUptimeScore(int lastDay, long totalDownTime) {
		double totalUptime = lastDay * 24 * 60;
		double uptimePercentage = (totalUptime - totalDownTime) * 100 / totalUptime;

		// 소수 둘째 자리까지 반올림
		return Math.round(uptimePercentage * 100.0) / 100.0;
	}

	public List<StatsDto> getStatistics(String serviceType, String period, String targetSystem) {
		List<StatsDto> StatsDtoList = statisticsRepository.getStatisticsByServiceTypeAndPeriodAndTargetSystem(
			serviceType, period, targetSystem);
		return StatsDtoList;

	}
}
