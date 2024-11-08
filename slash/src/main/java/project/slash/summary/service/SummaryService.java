package project.slash.summary.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.slash.contract.dto.response.EvaluationItemDto;
import project.slash.contract.model.ServiceTarget;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.summary.dto.ScoreDto;
import project.slash.summary.dto.SummaryDto;
import project.slash.summary.dto.evaluation.item.IncidentResolvedRateDto;
import project.slash.summary.mapper.SummaryMapper;
import project.slash.summary.model.Summary;
import project.slash.summary.repository.SummaryRepository;

@Service
@RequiredArgsConstructor
public class SummaryService {
	private final SummaryRepository summaryRepository;
	private final EvaluationItemRepository evaluationItemRepository;
	private final ServiceTargetRepository serviceTargetRepository;

	public void saveSummaries(List<SummaryDto> summaryDtos) {
		// Dto를 Entity로 매핑 후 저장
		List<Summary> summaries = summaryDtos.stream()
			.map(SummaryMapper.INSTANCE::toEntity)
			.collect(Collectors.toList());
		System.out.println("저장!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		for (Summary summary : summaries) {
			System.out.println(summary.toString());
		}
		// saveAll 메서드를 통해 리스트 전체를 저장
		summaryRepository.saveAll(summaries);
	}

	// 2. 필요한 데이터 조회하기.
	public List<SummaryDto> getSummary(Long evaluationItemId, String targetSystem,
		String targetEquipment, String lastDate) {
		List<SummaryDto> summaryDtos = new ArrayList<>();
		EvaluationItemDto evaluationItemDto = evaluationItemRepository.findEvaluationItem(evaluationItemId)
			.orElseThrow(() -> new RuntimeException("Evaluation Item not found"));
		Integer weight = evaluationItemDto.getWeight();

		// 서비스 가동률, 장애적기 처리율, 서비스요청 적기 처리율
		if (evaluationItemId == 1) {
			summaryRepository.getServiceRuntimeRate(evaluationItemId, targetSystem, targetEquipment, lastDate);
		} else if (evaluationItemId == 2) {
			IncidentResolvedRateDto incidentResolvedRateDto = summaryRepository.getIncidentResolvedRate(
				evaluationItemId, targetSystem, targetEquipment, lastDate);
			LocalDate localDate = LocalDate.parse(lastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			System.out.println(localDate);
			System.out.println(targetSystem);
			System.out.println(targetEquipment);
			ScoreDto scoreDto = getScore(evaluationItemId, weight,
				incidentResolvedRateDto.getRequestCount() - incidentResolvedRateDto.getDueOnTimeCount());
			summaryDtos.add(
				new SummaryDto(106L, Date.valueOf(localDate), scoreDto.getGrade(), scoreDto.getScore(), "월별",
					evaluationItemId,
					scoreDto.getWeightedScore(), false, scoreDto.getScore(), incidentResolvedRateDto.getTotalDownTime(),
					incidentResolvedRateDto.getRequestCount(), incidentResolvedRateDto.getDueOnTimeCount(),
					targetSystem,
					evaluationItemDto.getCategory(),
					targetEquipment, false));
		} else if (evaluationItemId == 3) {
			summaryRepository.getServiceResolvedRate(evaluationItemId, targetSystem, targetEquipment, lastDate);
		}

		// 4. Dto를 모아 DB에 저장하기.
		saveSummaries(summaryDtos);
		return summaryDtos;

	}

	private ScoreDto getScore(Long evaluationItemId, Integer weight, double score) {
		List<ServiceTarget> serviceTargetList = serviceTargetRepository.findByEvaluationItemId(evaluationItemId);
		Integer totalWeights = evaluationItemRepository.getTotalWeight(evaluationItemId);

		for (ServiceTarget serviceTarget : serviceTargetList) {
			String grade = serviceTarget.getGrade();
			double min = serviceTarget.getMin();
			double max = serviceTarget.getMax();
			boolean minInclusive = serviceTarget.isMinInclusive();
			boolean maxInclusive = serviceTarget.isMaxInclusive();
			if (isInTargetRange(max, maxInclusive, min, minInclusive, score)) {
				return new ScoreDto(score, grade, getWeightedScore(weight, totalWeights, score), evaluationItemId);
			}
		}
		return null;
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
