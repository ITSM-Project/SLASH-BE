package project.slash.statistics.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.ContractDataDto;
import project.slash.contract.model.ServiceTarget;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.statistics.dto.GradeScoreDto;
import project.slash.statistics.dto.IncidentInfoDto;
import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.model.Statistics;
import project.slash.statistics.repository.StatisticsRepository;
import project.slash.taskrequest.repository.TaskRequestRepository;

@Service
@RequiredArgsConstructor
public class StatisticsService {
	private final StatisticsRepository statisticsRepository;
	private final ContractRepository contractRepository;
	private final ServiceTargetRepository serviceTargetRepository;
	private final TaskRequestRepository taskRequestRepository;
	private final EvaluationItemRepository evaluationItemRepository;

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
					new MonthlyServiceStatisticsDto(date, serviceType, monthlyDataDto.getEquipmentName(), grade, score,
						"월별",
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

	@Transactional
	public void getIncidentStatistics(RequestStatisticsDto requestStatisticsDto) {

		// 장애 건수와 적기 처리 건수 계산
		IncidentInfoDto incidentInfoDto = taskRequestRepository.getIncidentCount(
			requestStatisticsDto.getEvaluationItemId(),
			requestStatisticsDto.getEndDate());
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
			Statistics.fromIncidentInfo(incidentInfoDto, requestStatisticsDto.getEndDate(), gradeScoreDto.getScore(),
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
}
