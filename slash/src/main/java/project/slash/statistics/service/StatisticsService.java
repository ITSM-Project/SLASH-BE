package project.slash.statistics.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.ContractDataDto;
import project.slash.contract.model.Contract;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.statistics.dto.response.EvaluatedDto;
import project.slash.statistics.dto.response.MonthlyDataDto;
import project.slash.statistics.dto.response.MonthlyStatisticsDto;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.statistics.dto.response.IndicatorExtraInfoDto;
import project.slash.statistics.dto.response.IndicatorDto;
import project.slash.statistics.dto.response.MonthlyIndicatorsDto;
import project.slash.statistics.model.Statistics;
import project.slash.statistics.repository.StatisticsRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {
	private static final int MINIMUM_STATISTICS_REQUIRED = 3;

	private final StatisticsRepository statisticsRepository;
	private final ContractRepository contractRepository;
	private final EvaluationItemRepository evaluationItemRepository;
	private final TotalTargetRepository totalTargetRepository;

	public void createMonthlyStats(LocalDate date, long evaluationItemId) {
		List<MonthlyStatisticsDto> monthlyStatisticsDtoList = calculateMonthlyStats(date,
			evaluationItemId);
		statisticsRepository.saveMonthlyData(monthlyStatisticsDtoList);
	}

	// 자동 계산 로직
	public List<MonthlyStatisticsDto> calculateMonthlyStats(LocalDate date, long evaluationItemId) {
		List<MonthlyDataDto> monthlyData = statisticsRepository.getMonthlyData(date);
		Optional<EvaluationItem> evaluationItem = evaluationItemRepository.findById(evaluationItemId);
		Long contractId = evaluationItem
			.map(EvaluationItem::getContract)
			.map(Contract::getId)
			.orElse(null);
		String category = evaluationItem
			.map(EvaluationItem::getCategory) // EvaluationItem 객체가 존재할 경우 category 반환
			.orElse(null);
		List<ContractDataDto> contractData = contractRepository.findIndicatorByEvaluationItemId(evaluationItemId,
			contractId);
		List<MonthlyStatisticsDto> result = new ArrayList<>();
		for (MonthlyDataDto monthlyDataDto : monthlyData) {
			EvaluatedDto evaluatedDto = calculateScoreAndEvaluate(monthlyDataDto, contractData, category);
			MonthlyStatisticsDto monthlyStatisticsDto = makeConstructByServiceType(date, category,
				monthlyDataDto, evaluatedDto);
			result.add(monthlyStatisticsDto);
		}
		return result;
	}

	private EvaluatedDto calculateScoreAndEvaluate(MonthlyDataDto monthlyDataDto, List<ContractDataDto> contractData,
		String category) {
		int day = monthlyDataDto.getSelectDay();
		double score = getScore(category, day,
			monthlyDataDto.getTotalDownTime());
		return evaluateWithIndicator(contractData, score);
	}

	// 각자 추가
	private MonthlyStatisticsDto makeConstructByServiceType(LocalDate date, String category,
		MonthlyDataDto monthlyDataDto, EvaluatedDto evaluatedDto) {
		//서비스 가동률은 적기 처리 건수 필요없어서 0으로 넣음
		if (category.equals("서비스 가동률")) {
			return new MonthlyStatisticsDto(
				date, category, monthlyDataDto.getEquipmentName(), evaluatedDto.getGrade(), evaluatedDto.getScore(),
				"월별", evaluatedDto.getWeightedScore(), false, monthlyDataDto.getTotalDownTime(),
				monthlyDataDto.getRequestCount(), evaluatedDto.getEvaluationItemId(), monthlyDataDto.getSystemName(),
				evaluatedDto.getScore(), monthlyDataDto.getSystemIncidentCount(), 0L, false
			);
		}

		return new MonthlyStatisticsDto();
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
	private double getScore(String category, int day, long totalDownTime) {
		if (category.equals("서비스 가동률")) {
			double totalUptime = day * 24 * 60;
			double uptimePercentage = (totalUptime - totalDownTime) * 100 / totalUptime;

			// 소수 둘째 자리까지 반올림
			return Math.round(uptimePercentage * 100.0) / 100.0;
		}
		return 0.0;
	}

	public MonthlyIndicatorsDto getMonthlyIndicators(Long contractId, int year, int month) {
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());

		List<Statistics> statistics = statisticsRepository.findByDateBetweenAndEvaluationItemsContractIdAndApprovalStatusTrue(
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
}
