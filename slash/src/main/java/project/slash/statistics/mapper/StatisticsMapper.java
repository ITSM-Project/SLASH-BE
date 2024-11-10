package project.slash.statistics.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.statistics.dto.response.CalculatedStatisticsDto;
import project.slash.statistics.model.Statistics;

@Component
public class StatisticsMapper {

	public List<CalculatedStatisticsDto> toCalculatedStatisticsList(List<Statistics> statistics) {
		return statistics.stream()
			.map(this::toCalculatedStatistics)
			.toList();
	}

	public CalculatedStatisticsDto toCalculatedStatistics(Statistics statistics) {
		return new CalculatedStatisticsDto(
			statistics.getId(),
			statistics.getEvaluationItem().getId(),
			statistics.getServiceType(),
			statistics.isAuto(),
			statistics.getDate(),
			statistics.isApprovalStatus()
		);
	}
}
