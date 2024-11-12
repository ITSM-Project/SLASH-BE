package project.slash.statistics.service;

import static project.slash.statistics.exception.StatisticsErrorCode.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.statistics.dto.response.MonthlyServiceStatisticsDto;
import project.slash.contract.mapper.EvaluationItemMapper;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.statistics.dto.request.EditStatisticsDto;
import project.slash.statistics.dto.response.CalculatedStatisticsDto;
import project.slash.statistics.dto.response.IndicatorExtraInfoDto;
import project.slash.statistics.dto.response.MonthlyIndicatorsDto;
import project.slash.statistics.dto.response.StatisticsStatusDto;
import project.slash.statistics.dto.response.UnCalculatedStatisticsDto;
import project.slash.statistics.mapper.StatisticsMapper;
import project.slash.statistics.model.Statistics;
import project.slash.statistics.repository.StatisticsRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticsService {
	private static final int MINIMUM_STATISTICS_REQUIRED = 3;

	private final StatisticsRepository statisticsRepository;
	private final EvaluationItemRepository evaluationItemRepository;
	private final TotalTargetRepository totalTargetRepository;

	private final EvaluationItemMapper evaluationItemMapper;
	private final StatisticsMapper statisticsMapper;

	public MonthlyIndicatorsDto getMonthlyIndicators(Long contractId, YearMonth date) {
		String targetSystem = "전체";
		LocalDate startDate = date.atDay(1);
		LocalDate endDate = date.atEndOfMonth();

		List<Statistics> statistics = statisticsRepository
			.findByDateBetweenAndEvaluationItemContractIdAndApprovalStatusTrueAndTargetSystem(startDate, endDate,
				contractId, targetSystem);

		if (statistics.size() < MINIMUM_STATISTICS_REQUIRED) {
			return new MonthlyIndicatorsDto();
		}

		return new MonthlyIndicatorsDto(getIndicatorExtraInfo(contractId, statistics),
			statisticsMapper.toMonthlyIndicators(statistics));
	}


	private IndicatorExtraInfoDto getIndicatorExtraInfo(Long contractId, List<Statistics> statistics) {
		double score = 0;
		long requestCount = 0;
		long incidentTime = 0;

		for (Statistics statistic : statistics) {
			score += statistic.getWeightedScore();
			requestCount += statistic.getRequestCount() + statistic.getSystemIncidentCount();
			incidentTime += statistic.getTotalDowntime();
		}

		return new IndicatorExtraInfoDto(findTotalTarget(contractId, score), score, requestCount, incidentTime);
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

	public StatisticsStatusDto getStatisticsStatus(Long contractId, LocalDate endDate) {
		LocalDate startDate = endDate.withDayOfMonth(1);

		List<EvaluationItem> unCalculatedEvaluationItem = evaluationItemRepository.findUnCalculatedEvaluationItem(contractId, endDate);
		List<UnCalculatedStatisticsDto> unCalculatedStatistics = evaluationItemMapper.unCalculatedStatisticsList(unCalculatedEvaluationItem);	//미계산 된 지표

		List<Statistics> statistics = statisticsRepository.findByDateBetweenAndEvaluationItemContractId(startDate, endDate, contractId);
		List<CalculatedStatisticsDto> calculatedStatistics = statistics.stream()	//계산된 지표중 전체 통계만 조회
			.filter(statistic -> statistic.getTargetSystem().equals("전체"))
			.map(statisticsMapper::toCalculatedStatistics)
			.toList();

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

	public List<MonthlyServiceStatisticsDto> getStatistics(Long evaluationItemId, LocalDate date) {
		List<Statistics> statistics = statisticsRepository.findByEvaluationItemIdAndCalculateTime(evaluationItemId, date);

		return statisticsMapper.toCalculatedStatisticsDtos(statistics);
	}
}
