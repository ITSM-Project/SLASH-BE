package project.slash.statistics.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.ContractDataDto;
import project.slash.contract.repository.ContractRepository;
import project.slash.statistics.dto.EvaluatedDto;
import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.statistics.repository.StatisticsRepository;

@Service
@RequiredArgsConstructor
public class StatisticsService {
	private final StatisticsRepository statisticsRepository;
	private final ContractRepository contractRepository;

	// 자동 통계 삽입
	public void createMonthlyStats(String serviceType, Boolean isAuto) {
		List<MonthlyServiceStatisticsDto> monthlyServiceStatisticsDtoList = calculateMonthlyStats(serviceType, isAuto);
		statisticsRepository.saveMonthlyData(monthlyServiceStatisticsDtoList);
	}

	// // 자동 계산 로직, 조건별 산출식 변경
	// public List<MonthlyServiceStatisticsDto> calculateMonthlyStats(String serviceType) {
	// 	List<MonthlyDataDto> monthlyData = statisticsRepository.getMonthlyData();
	// 	List<ContractDataDto> contractData = contractRepository.findIndicatorByCategory(serviceType);
	// 	List<MonthlyServiceStatisticsDto> result = new ArrayList<>();
	//
	// 	for (MonthlyDataDto monthlyDataDto : monthlyData) {
	// 		double score = 0.0;
	// 		//이부분은 서비스타입별로 다륾
	// 		if (serviceType.equals("서비스 가동률")) {
	// 			score = getServiceUptimeScore(monthlyDataDto.getLastDay(), monthlyDataDto.getTotalDownTime());
	// 		}
	// 		String grade = null;
	// 		double weightedScore = 0.0;
	// 		long EvaluationItemId = 0L;
	// 		for (ContractDataDto contractDataDto : contractData) {
	// 			Boolean isInTargetRange = isInTargetRange(
	// 				contractDataDto.getMax(),
	// 				contractDataDto.isMaxInclusive(),
	// 				contractDataDto.getMin(),
	// 				contractDataDto.isMinInclusive(),
	// 				score
	// 			);
	// 			if (isInTargetRange) {
	// 				grade = contractDataDto.getGrade();
	// 				EvaluationItemId = contractDataDto.getEvaluationItemId();
	// 				weightedScore = getWeightedScore(contractDataDto.getWeight(), contractDataDto.getWeightTotal(),
	// 					score);
	// 				break;
	// 			}
	// 		}
	// 		String yearMonthString = monthlyDataDto.getYearMonth();
	// 		LocalDate date = LocalDate.parse(yearMonthString + "-" + monthlyDataDto.getLastDay(),
	// 			DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	//
	// 		//이부분은 서비스 타입 별로 다륾
	// 		// 서비스 가동률의 경우 , 아래에 if문 추가하면 됨
	// 		if (serviceType.equals("서비스 가동률")) {
	// 			result.add(
	// 				new MonthlyServiceStatisticsDto(date, serviceType, monthlyDataDto.getEquipmentName(), grade, score, "월별",
	// 					weightedScore, true,
	// 					monthlyDataDto.getTotalDownTime(), monthlyDataDto.getRequestCount(), EvaluationItemId,
	// 					monthlyDataDto.getSystemName(), score, monthlyDataDto.getSystemIncidentCount(), 0L));
	// 		}
	//
	// 	}
	//
	// 	return result;
	// }

	// 자동 계산 로직
	public List<MonthlyServiceStatisticsDto> calculateMonthlyStats(String serviceType, Boolean isAuto) {
		List<MonthlyDataDto> monthlyData = statisticsRepository.getMonthlyData();
		List<ContractDataDto> contractData = contractRepository.findIndicatorByCategory(serviceType);
		List<MonthlyServiceStatisticsDto> result = new ArrayList<>();
		for (MonthlyDataDto monthlyDataDto : monthlyData) {
			EvaluatedDto evaluatedDto = calculateScoreAndEvaluate(monthlyDataDto, contractData, serviceType);
			LocalDate date = parsingDate(monthlyDataDto.getYearMonth(), monthlyDataDto.getLastDay());
			MonthlyServiceStatisticsDto monthlyServiceStatisticsDto = makeConstructByServiceType(date, serviceType,
				monthlyDataDto, evaluatedDto, isAuto);
			result.add(monthlyServiceStatisticsDto);
		}
		return result;
	}

	private EvaluatedDto calculateScoreAndEvaluate(MonthlyDataDto monthlyDataDto, List<ContractDataDto> contractData,
		String serviceType) {
		double score = getScore(serviceType, monthlyDataDto.getLastDay(),
			monthlyDataDto.getTotalDownTime());
		return evaluateWithIndicator(contractData, score);
	}

	// 각자 추가
	private MonthlyServiceStatisticsDto makeConstructByServiceType(LocalDate date, String serviceType,
		MonthlyDataDto monthlyDataDto, EvaluatedDto evaluatedDto, Boolean isAuto) {

		//서비스 가동률은 적기 처리 건수 필요없어서 0으로 넣음
		if (serviceType.equals("서비스 가동률")) {
			return new MonthlyServiceStatisticsDto(
				date, serviceType, monthlyDataDto.getEquipmentName(), evaluatedDto.getGrade(), evaluatedDto.getScore(),
				"월별", evaluatedDto.getWeightedScore(), true, monthlyDataDto.getTotalDownTime(),
				monthlyDataDto.getRequestCount(), evaluatedDto.getEvaluationItemId(), monthlyDataDto.getSystemName(),
				evaluatedDto.getScore(), monthlyDataDto.getSystemIncidentCount(), 0L, isAuto
			);
		}

		return new MonthlyServiceStatisticsDto();
	}

	private LocalDate parsingDate(String yearMonth, int lastDay) {
		return LocalDate.parse(yearMonth + "-" + lastDay,
			DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	private EvaluatedDto evaluateWithIndicator(List<ContractDataDto> contractData, double score) {
		EvaluatedDto evaluatedDto = new EvaluatedDto(null, 0L, 0L, 0.0);
		for (ContractDataDto contractDataDto : contractData) {
			Boolean isInTargetRange = isInTargetRange(contractDataDto.getMax(), contractDataDto.isMaxInclusive(),
				contractDataDto.getMin(), contractDataDto.isMinInclusive(), score
			);
			if (isInTargetRange) {
				evaluatedDto = new EvaluatedDto(
					contractDataDto.getGrade()
					, getWeightedScore(contractDataDto.getWeight(),
					contractDataDto.getWeightTotal(), score), contractDataDto.getEvaluationItemId(), score);
				break;
			}
		}
		return evaluatedDto;
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

	// 각자 추가
	private double getScore(String serviceType, int lastDay, long totalDownTime) {
		if (serviceType.equals("서비스 가동률")) {
			double totalUptime = lastDay * 24 * 60;
			double uptimePercentage = (totalUptime - totalDownTime) * 100 / totalUptime;

			// 소수 둘째 자리까지 반올림
			return Math.round(uptimePercentage * 100.0) / 100.0;
		}
		return 0.0;
	}

	public List<StatisticsDto> getStatistics(String serviceType, String period, String targetSystem,
		String targetEquipment) {
		return statisticsRepository.getStatistics(
			serviceType, period, targetSystem, targetEquipment);
	}
}
