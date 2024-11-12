package project.slash.statistics.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.statistics.dto.response.CalculatedStatisticsDto;
import project.slash.statistics.dto.response.IndicatorDto;
import project.slash.statistics.dto.response.MonthlyServiceStatisticsDto;
import project.slash.statistics.model.Statistics;

@Component
public class StatisticsMapper {
	public CalculatedStatisticsDto toCalculatedStatistics(Statistics statistics) {
		return new CalculatedStatisticsDto(
			statistics.getId(),
			statistics.getEvaluationItem().getId(),
			statistics.getServiceType(),
			statistics.isAuto(),
			statistics.getDate(),
			statistics.getCalculateTime(),
			statistics.isApprovalStatus()
		);
	}

	public List<MonthlyServiceStatisticsDto> toCalculatedStatisticsDtos(List<Statistics> statistics) {
		return statistics.stream()
			.map(this::toMonthlyServiceStatisticsDto)
			.toList();
	}

	public MonthlyServiceStatisticsDto toMonthlyServiceStatisticsDto(Statistics statistics) {
		return MonthlyServiceStatisticsDto.builder()
			.date(statistics.getDate())
			.calculateTime(statistics.getCalculateTime())
			.serviceType(statistics.getServiceType())
			.targetEquipment(statistics.getTargetEquipment())
			.grade(statistics.getGrade())
			.score(statistics.getScore())
			.period(statistics.getPeriod())
			.weightedScore(statistics.getWeightedScore())
			.approvalStatus(statistics.isApprovalStatus())
			.totalDowntime(statistics.getTotalDowntime())
			.requestCount(statistics.getRequestCount())
			.targetSystem(statistics.getTargetSystem())
			.estimate(statistics.getEstimate())
			.systemIncidentCount(statistics.getSystemIncidentCount())
			.dueOnTimeCount(statistics.getDueOnTimeCount())
			.build();
	}

	public List<IndicatorDto> toMonthlyIndicators(List<Statistics> statistics) {
		return statistics.stream()
			.map(IndicatorDto::of)
			.toList();
	}
}
