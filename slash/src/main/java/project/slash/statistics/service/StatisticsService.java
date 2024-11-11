package project.slash.statistics.service;

import static project.slash.contract.exception.EvaluationItemErrorCode.*;
import static project.slash.statistics.exception.StatisticsErrorCode.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.response.ContractDataDto;
import project.slash.contract.model.ServiceTarget;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.statistics.dto.GradeScoreDto;
import project.slash.statistics.dto.IncidentInfoDto;
import project.slash.statistics.dto.response.EvaluatedDto;
import project.slash.statistics.dto.response.MonthlyDataDto;
import project.slash.statistics.dto.response.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.response.MonthlyStatisticsDto;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.dto.response.ResponseServiceTaskDto;
import project.slash.statistics.dto.response.ResponseStatisticsDto;
import project.slash.contract.mapper.EvaluationItemMapper;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.TotalTargetRepository;
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
import project.slash.taskrequest.repository.TaskRequestRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticsService {
	private static final int MINIMUM_STATISTICS_REQUIRED = 3;

	private final StatisticsRepository statisticsRepository;
	private final ContractRepository contractRepository;
	private final ServiceTargetRepository serviceTargetRepository;
	private final TaskRequestRepository taskRequestRepository;
	private final EvaluationItemRepository evaluationItemRepository;
	private final TotalTargetRepository totalTargetRepository;


	private final EvaluationItemMapper evaluationItemMapper;
	private final StatisticsMapper statisticsMapper;

	@Transactional
	public void createMonthlyStats(LocalDate date, long evaluationItemId) {
		List<MonthlyStatisticsDto> monthlyStatisticsDtoList = calculateMonthlyStats(date, evaluationItemId);
		MonthlyStatisticsDto monthlyStatisticsDto = getEntireStatistics(monthlyStatisticsDtoList);
		monthlyStatisticsDtoList.add(monthlyStatisticsDto);

		statisticsRepository.saveMonthlyData(monthlyStatisticsDtoList);

	}

	public MonthlyStatisticsDto getEntireStatistics(List<MonthlyStatisticsDto> monthlyStatisticsDtoList) {
		long evaluationItemId = monthlyStatisticsDtoList.get(0).getEvaluationItemId();

		long totalRequestCount = 0L;
		double totalScore = 0.0;
		double totalWeightedScore = 0.0;
		long totalDownTime = 0L;
		long totalSystemIncidentCount = 0L;
		double totalEstimate = 0.0;

		for (MonthlyStatisticsDto monthlyStatisticsDto : monthlyStatisticsDtoList) {
			totalRequestCount += monthlyStatisticsDto.getRequestCount();
			totalScore += monthlyStatisticsDto.getScore();
			totalWeightedScore += monthlyStatisticsDto.getWeightedScore();
			totalDownTime += monthlyStatisticsDto.getTotalDowntime();
			totalSystemIncidentCount += monthlyStatisticsDto.getSystemIncidentCount();
			totalEstimate += monthlyStatisticsDto.getEstimate();
		}

		double averageScore = Math.round((totalScore / monthlyStatisticsDtoList.size())*100.0)/ 100.0;
		double averageWeightedScore = Math.round((totalWeightedScore / monthlyStatisticsDtoList.size())*100.0)/ 100.0;
		double averageEstimate = Math.round((totalEstimate / monthlyStatisticsDtoList.size())*100.0)/ 100.0;


		List<ContractDataDto> contractDataDto = getContractDataDto(evaluationItemId);
		// 등급 계산
		EvaluatedDto evaluatedDto = evaluateWithIndicator(contractDataDto, averageScore);

		return new MonthlyStatisticsDto(
			monthlyStatisticsDtoList.get(0).getDate(),
			monthlyStatisticsDtoList.get(0).getServiceType(),
			"전체",
			evaluatedDto.getGrade(),
			averageScore,
			"월별",
			averageWeightedScore,
			false,
			totalDownTime,
			totalRequestCount,
			evaluationItemId,
			"전체",
			averageEstimate,
			totalSystemIncidentCount,
			0L,
			true
		);
	}

	public List<ContractDataDto> getContractDataDto(long evaluationItemId) {
		Optional<EvaluationItem> evaluationItem = evaluationItemRepository.findById(evaluationItemId);
		EvaluationItem item = evaluationItem
			.orElseThrow(() -> new BusinessException(NOT_FOUND_ITEMS));
		long contractId = item.getContract().getId();
		return contractRepository.findContractByEvaluationItemId(evaluationItemId,
			contractId);
	}

	// 자동 계산 로직
	public List<MonthlyStatisticsDto> calculateMonthlyStats(LocalDate date, long evaluationItemId) {
		List<MonthlyDataDto> monthlyData = statisticsRepository.getMonthlyData(date);
		List<ContractDataDto> contractDataDto=getContractDataDto(evaluationItemId);
		List<MonthlyStatisticsDto> result = new ArrayList<>();
		for (MonthlyDataDto monthlyDataDto : monthlyData) {
			EvaluatedDto evaluatedDto = calculateScoreAndEvaluate(monthlyDataDto, contractDataDto);
			result.add(new MonthlyStatisticsDto(
				date, contractDataDto.get(0).getCategory(), monthlyDataDto.getEquipmentName(), evaluatedDto.getGrade(), evaluatedDto.getScore(),
				"월별", evaluatedDto.getWeightedScore(), false, monthlyDataDto.getTotalDownTime(),
				monthlyDataDto.getRequestCount(), evaluatedDto.getEvaluationItemId(), monthlyDataDto.getSystemName(),
				evaluatedDto.getScore(), monthlyDataDto.getSystemIncidentCount(), 0L, true
			));
		}
		return result;
	}

	private EvaluatedDto calculateScoreAndEvaluate(MonthlyDataDto monthlyDataDto, List<ContractDataDto> contractData) {
		int day = monthlyDataDto.getSelectDay();
		double score = getScore(day,
			monthlyDataDto.getTotalDownTime());
		return evaluateWithIndicator(contractData, score);
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

	private double getScore(int day, long totalDownTime) {

		double totalUptime = day * 24 * 60;
		double uptimePercentage = (totalUptime - totalDownTime) * 100 / totalUptime;

		// 소수 둘째 자리까지 반올림
		return Math.round(uptimePercentage * 100.0) / 100.0;

	}


	@Transactional
	public void getIncidentStatistics(RequestStatisticsDto requestStatisticsDto) {

		// 장애 건수와 적기 처리 건수 계산
		IncidentInfoDto incidentInfoDto = taskRequestRepository.getIncidentCount(
			requestStatisticsDto.getEvaluationItemId(),
			requestStatisticsDto.getDate());
		long incidentCount = incidentInfoDto.getTotalOverdueCount();

		// 점수와 등급 리스트
		List<ServiceTarget> gradeList = serviceTargetRepository.getServiceTargetByEvaluationItem_Id(
			requestStatisticsDto.getEvaluationItemId());

		// 환산 점수와 등급
		GradeScoreDto gradeScoreDto = getGradeAndScore(incidentCount, gradeList);
		int weight = evaluationItemRepository.getReferenceById(requestStatisticsDto.getEvaluationItemId())
			.getWeight();
		int totalWeight = evaluationItemRepository.findTotalWeightByEvaluationItemId(
			requestStatisticsDto.getEvaluationItemId());
		double weightScore = getWeightedScore(gradeScoreDto.getScore(), weight, totalWeight);

		// 저장
		statisticsRepository.save(
			Statistics.fromIncidentInfo(incidentInfoDto, requestStatisticsDto.getDate(), gradeScoreDto.getScore(),
				weightScore, gradeScoreDto.getGrade(), gradeScoreDto.getScore(),
				evaluationItemRepository.getReferenceById(requestStatisticsDto.getEvaluationItemId())));
	}

	public GradeScoreDto getGradeAndScore(long score, List<ServiceTarget> gradeList) {
		return gradeList.stream()
			.filter(serviceTarget -> isScoreInRange(score, serviceTarget))
			.findFirst()
			.map(serviceTarget -> new GradeScoreDto(serviceTarget.getGrade(), (int)serviceTarget.getScore()))
			.orElseThrow(() -> new IllegalArgumentException("적절한 범위가 없습니다!, 점수: " + score));
	}

	private boolean isScoreInRange(long score, ServiceTarget serviceTarget) {
		double min = serviceTarget.getMin();
		double max = serviceTarget.getMax();
		return (serviceTarget.isMinInclusive() ? score >= min : score > min) &&
			(serviceTarget.isMaxInclusive() ? score <= max : score < max);
  }

	@Transactional
	public void createServiceTaskStatistics(RequestStatisticsDto requestStatisticsDto) {
		ResponseServiceTaskDto responseServiceTaskDto = statisticsRepository.getServiceTaskStatics(
			requestStatisticsDto.getEvaluationItemId(), requestStatisticsDto.getDate());
		double score = Math.round(
			(double)responseServiceTaskDto.getDueOnTimeCount() / responseServiceTaskDto.getTaskRequest() * 10000)
			/ 100.0;
		double weightScore = Math.round(
			score / responseServiceTaskDto.getTotalWeight() * responseServiceTaskDto.getEvaluationItem().getWeight()
				* 100) / 100.0;
		String grade = getGrade(responseServiceTaskDto.getEvaluationItem().getId(), score);
		Statistics statistics = Statistics.fromResponseServiceTask(responseServiceTaskDto,
			requestStatisticsDto.getDate(), score, weightScore, grade);
		statisticsRepository.save(statistics);
	}

	//등급산출
	public String getGrade(Long evaluationItemId, double score) {
		return serviceTargetRepository.getServiceTargetByEvaluationItem_Id(
				evaluationItemId)
			.stream()
			.filter(serviceTarget ->
				(serviceTarget.isMinInclusive() ? score >= serviceTarget.getMin() : score > serviceTarget.getMin()) &&
					(serviceTarget.isMaxInclusive() ? score <= serviceTarget.getMax() : score < serviceTarget.getMax())
			)
			.map(ServiceTarget::getGrade)  // 조건을 만족하는 ServiceTarget의 grade 값을 추출
			.findFirst()
			.orElse(null);
	}


	public ResponseStatisticsDto getServiceStatistics(Long evaluationItemId, LocalDate date) {
		ResponseServiceTaskDto responseServiceTaskDto = statisticsRepository.getServiceTaskStatics(
			evaluationItemId, date);
		double score = Math.round(
			(double)responseServiceTaskDto.getDueOnTimeCount() / responseServiceTaskDto.getTaskRequest() * 10000)
			/ 100.0;
		double weightScore = Math.round(
			score / responseServiceTaskDto.getTotalWeight() * responseServiceTaskDto.getEvaluationItem().getWeight()
				* 100) / 100.0;
		String grade = getGrade(responseServiceTaskDto.getEvaluationItem().getId(), score);
		return ResponseStatisticsDto.fromResponseServiceTask(responseServiceTaskDto, score, weightScore, grade);
  }

	public MonthlyIndicatorsDto getMonthlyIndicators(Long contractId, YearMonth date) {
		LocalDate startDate = date.atDay(1);
		LocalDate endDate = date.atEndOfMonth();

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

	public StatisticsStatusDto getStatisticsStatus(Long contractId, LocalDate endDate) {
		LocalDate startDate = endDate.withDayOfMonth(1);

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

	public List<MonthlyServiceStatisticsDto> getStatistics(Long evaluationItemId, LocalDate date) {
		List<Statistics> statistics = statisticsRepository.findByEvaluationItemIdAndDateAndApprovalStatusTrue(evaluationItemId, date);

		return statisticsMapper.toCalculatedStatisticsDtos(statistics);
	}
}
