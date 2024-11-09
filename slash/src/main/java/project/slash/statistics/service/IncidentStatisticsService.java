package project.slash.statistics.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.contract.model.ServiceTarget;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.statistics.dto.GradeScoreDto;
import project.slash.statistics.dto.IncidentInfoDto;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.model.Statistics;
import project.slash.statistics.repository.StatisticsRepository;
import project.slash.taskrequest.repository.TaskRequestRepository;

@RequiredArgsConstructor
@Service
public class IncidentStatisticsService {
	private final StatisticsRepository statisticsRepository;
	private final ServiceTargetRepository serviceTargetRepository;
	private final TaskRequestRepository taskRequestRepository;
	private final EvaluationItemRepository evaluationItemRepository;

	@Transactional
	public void getIncidentStatistics(RequestStatisticsDto requestStatisticsDto) {

		// 장애 건수와 적기 처리 건수 계산
		IncidentInfoDto incidentInfoDto = taskRequestRepository.getIncidentCount(
			requestStatisticsDto.getEvaluationItemId(),
			requestStatisticsDto.getEndDate());
		long incidentScore = incidentInfoDto.getTotalOverdueCount();

		// 점수와 등급 리스트
		List<ServiceTarget> gradeList = serviceTargetRepository.getServiceTargetByEvaluationItem_Id(
			requestStatisticsDto.getEvaluationItemId());

		// 환산 점수와 등급
		GradeScoreDto gradeScoreDto = getGradeAndScore(incidentScore, gradeList);
		int weight = evaluationItemRepository.getReferenceById(requestStatisticsDto.getEvaluationItemId())
			.getWeight();
		int totalWeight = evaluationItemRepository.findTotalWeightByEvaluationItemId(
			requestStatisticsDto.getEvaluationItemId());
		double weightScore = getWeightScore(gradeScoreDto.getScore(), weight, totalWeight);

		// 저장
		statisticsRepository.save(
			Statistics.fromIncidentInfo(incidentInfoDto, requestStatisticsDto.getEndDate(), gradeScoreDto.getScore(),
				weightScore, gradeScoreDto.getGrade(), gradeScoreDto.getScore(),
				evaluationItemRepository.getReferenceById(requestStatisticsDto.getEvaluationItemId())));
	}

	public double getWeightScore(double score, int weight, int totalWeight) {
		return Math.round(score * ((double)weight / totalWeight) * 100.0) / 100.0;
	}

	public GradeScoreDto getGradeAndScore(long score, List<ServiceTarget> gradeList) {
		return gradeList.stream()
			.filter(serviceTarget -> isScoreInRange(score, serviceTarget))
			.findFirst()
			.map(serviceTarget -> new GradeScoreDto(serviceTarget.getGrade(), serviceTarget.getScore()))
			.orElseThrow(() -> new IllegalArgumentException("적절한 범위가 없습니다!, 점수: " + score));
	}

	private boolean isScoreInRange(long score, ServiceTarget serviceTarget) {
		double min = serviceTarget.getMin();
		double max = serviceTarget.getMax();
		return (serviceTarget.isMinInclusive() ? score >= min : score > min) &&
			(serviceTarget.isMaxInclusive() ? score <= max : score < max);
	}

}
