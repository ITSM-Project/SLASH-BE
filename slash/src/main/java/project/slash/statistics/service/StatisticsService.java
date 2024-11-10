package project.slash.statistics.service;

import static project.slash.statistics.exception.StatisticsErrorCode.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.ContractDataDto;
import project.slash.contract.mapper.EvaluationItemMapper;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.statistics.dto.request.EditStatisticsDto;
import project.slash.statistics.dto.response.CalculatedStatisticsDto;
import project.slash.statistics.dto.response.IndicatorExtraInfoDto;
import project.slash.statistics.dto.response.IndicatorDto;
import project.slash.statistics.dto.response.MonthlyIndicatorsDto;
import project.slash.statistics.dto.response.StatisticsStatusDto;
import project.slash.statistics.dto.response.UnCalculatedStatisticsDto;
import project.slash.statistics.mapper.StatisticsMapper;
import project.slash.statistics.model.Statistics;
import project.slash.statistics.repository.StatisticsRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {
	private static final int MINIMUM_STATISTICS_REQUIRED = 3;

	private final StatisticsRepository statisticsRepository;
	private final ContractRepository contractRepository;
	private final TotalTargetRepository totalTargetRepository;
	private final EvaluationItemRepository evaluationItemRepository;

	private final EvaluationItemMapper evaluationItemMapper;
	private final StatisticsMapper statisticsMapper;

	public void createMonthlyStats(String serviceType) {
		List<MonthlyServiceStatisticsDto> monthlyServiceStatisticsDtoList = calculateMonthlyStats(serviceType);
		statisticsRepository.saveMonthlyData(monthlyServiceStatisticsDtoList);
	}

	// 자동 계산 로직, 조건별 산출식 변경
	public List<MonthlyServiceStatisticsDto> calculateMonthlyStats(String serviceType) {
		List<MonthlyDataDto> monthlyData = statisticsRepository.getMonthlyData();
		List<ContractDataDto> contractData = contractRepository.findIndicatorByCategory(serviceType);
		List<MonthlyServiceStatisticsDto> result = new ArrayList<>();

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
			LocalDate date = LocalDate.parse(yearMonthString + "-" + monthlyDataDto.getLastDay(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			//이부분은 서비스 타입 별로 다륾
			// 서비스 가동률의 경우 , 아래에 if문 추가하면 됨
			if (serviceType.equals("서비스 가동률")) {
				result.add(
					new MonthlyServiceStatisticsDto(date, serviceType, monthlyDataDto.getEquipmentName(), grade, score, "월별",
						weightedScore, true,
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

	public List<StatisticsDto> getStatistics(String serviceType, String period, String targetSystem,
		String targetEquipment) {
		String defaultServiceType = serviceType != null ? serviceType : "전체";
		String defaultPeriod = period != null ? period : "전체";
		String defaultTargetSystem = targetSystem != null ? targetSystem : "전체";
		String defaultTargetEquipment = targetEquipment != null ? targetEquipment : "전체";

		return statisticsRepository.getStatistics(
			serviceType, period, targetSystem, targetEquipment);
	}

	public MonthlyIndicatorsDto getMonthlyIndicators(Long contractId, int year, int month) {
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());

		List<Statistics> statistics = statisticsRepository.findByDateBetweenAndEvaluationItemContractIdAndApprovalStatusTrue(
			startDate, endDate, contractId);

		if(statistics.size() < MINIMUM_STATISTICS_REQUIRED) {
			return new MonthlyIndicatorsDto();
		}

		return new MonthlyIndicatorsDto(getIndicatorExtraInfo(contractId, statistics),
			getMonthlyIndicators(statistics));
	}

	private IndicatorExtraInfoDto getIndicatorExtraInfo(Long contractId, List<Statistics> statistics) {
		double score = 0;
		long requestCount = 0;
		long incidentTime = 0;

		for (Statistics statistic : statistics) {
			score += statistic.getScore();
			requestCount += statistic.getRequestCount() + statistic.getSystemIncidentCount();
			incidentTime += statistic.getTotalDowntime();
		}

		return new IndicatorExtraInfoDto(findTotalTarget(contractId, score), requestCount, incidentTime);
	}

	public String findTotalTarget(Long contractId, double score) {
		return totalTargetRepository.findByContractIdOrderByMinAsc(contractId).stream()
			.filter(target ->
				(target.isMinInclusive() ? score >= target.getMin() : score > target.getMin()) &&
					(target.isMaxInclusive() ? score <= target.getMax() : score < target.getMax())
			)
			.map(TotalTarget::getGrade)
			.findFirst()
			.orElse(null);
	}

	private static List<IndicatorDto> getMonthlyIndicators(List<Statistics> statistics) {
		return statistics.stream()
			.filter(s -> s.getTargetSystem().equals("전체"))
			.map(IndicatorDto::of)
			.toList();
	}

	public StatisticsStatusDto getStatisticsStatus(Long contractId, int year, int month, int day) {
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = LocalDate.of(year, month, day);

		List<EvaluationItem> unCalculatedEvaluationItem = evaluationItemRepository.findUnCalculatedEvaluationItem(contractId, endDate);
		List<UnCalculatedStatisticsDto> unCalculatedStatistics = evaluationItemMapper.unCalculatedStatisticsList(unCalculatedEvaluationItem);	//미계산된 지표

		List<Statistics> statistics = statisticsRepository.findByDateBetweenAndEvaluationItemContractId(startDate, endDate, contractId);
		List<CalculatedStatisticsDto> calculatedStatistics = statisticsMapper.toCalculatedStatisticsList(statistics); // 계산된 지표

		return new StatisticsStatusDto(unCalculatedStatistics, calculatedStatistics);
	}

	@Transactional
	public void approve(Long statisticsId, Long evaluationItemId) {
		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.withDayOfMonth(1);

		//이미 동일한 항목에 대한 승인된 지표가 있는 경우
		if (statisticsRepository.findByEvaluationItemIdAndApprovalStatusTrueAndDateBetween(evaluationItemId, startDate, endDate).isPresent()) {
			throw new BusinessException(STATISTICS_ALREADY_EXISTS);
		}

		Statistics statistics = findStatistics(statisticsId);
		statistics.approve();
	}

	@Transactional
	public void editStatistics(Long statisticsId, EditStatisticsDto editStatisticsDto) {
		Statistics statistics = findStatistics(statisticsId);
		statistics.update(editStatisticsDto.getGrade(), editStatisticsDto.getScore(), editStatisticsDto.getWeightedScore());
	}

	private Statistics findStatistics(Long statisticsId) {
		return statisticsRepository.findById(statisticsId).orElseThrow(() -> new BusinessException(NOT_FOUND_STATISTICS));
	}
}
