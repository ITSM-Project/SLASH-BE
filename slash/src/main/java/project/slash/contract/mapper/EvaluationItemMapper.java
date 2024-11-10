package project.slash.contract.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.contract.model.EvaluationItem;
import project.slash.statistics.dto.response.UnCalculatedStatisticsDto;

@Component
public class EvaluationItemMapper {
	public List<UnCalculatedStatisticsDto> unCalculatedStatisticsList(List<EvaluationItem> evaluationItems){
		return evaluationItems.stream()
			.map(this::toUnCalculatedStatistics)
			.toList();
	}

	public UnCalculatedStatisticsDto toUnCalculatedStatistics(EvaluationItem evaluationItem) {
		return new UnCalculatedStatisticsDto(evaluationItem.getId(), evaluationItem.getCategory());
	}
}
