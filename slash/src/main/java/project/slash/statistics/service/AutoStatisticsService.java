package project.slash.statistics.service;

import static project.slash.contract.exception.EvaluationItemErrorCode.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.response.ContractDataDto;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.ServiceTarget;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.contract.repository.contract.ContractRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.statistics.dto.GradeScoreDto;
import project.slash.statistics.dto.IncidentInfoDto;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.dto.response.EvaluatedDto;
import project.slash.statistics.dto.response.MonthlyDataDto;
import project.slash.statistics.dto.response.MonthlyStatisticsDto;
import project.slash.statistics.dto.response.ResponseServiceTaskDto;
import project.slash.statistics.dto.response.ResponseStatisticsDto;
import project.slash.statistics.model.Statistics;
import project.slash.statistics.repository.StatisticsRepository;
import project.slash.taskrequest.repository.TaskRequestRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutoStatisticsService {

	private final ContractRepository contractRepository;
	private final ServiceTargetRepository serviceTargetRepository;
	private final TaskRequestRepository taskRequestRepository;
	private final StatisticsRepository statisticsRepository;
	private final EvaluationItemRepository evaluationItemRepository;

	@Transactional
	public void createMonthlyStats(RequestStatisticsDto requestStatisticsDto) {
		List<MonthlyStatisticsDto> monthlyStatisticsDtoList = calculateMonthlyStats(requestStatisticsDto.getDate(),
			requestStatisticsDto.getEvaluationItemId());
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
}
