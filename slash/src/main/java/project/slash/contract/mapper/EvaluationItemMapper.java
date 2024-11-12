package project.slash.contract.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.contract.dto.TaskTypeDto;
import project.slash.contract.dto.request.CreateEvaluationItemDto;
import project.slash.contract.dto.response.EvaluationItemCategoryDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.dto.response.EvaluationItemDto;
import project.slash.contract.model.Contract;
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

	public List<EvaluationItemCategoryDto> toEvaluationItemCategoryDtos(List<EvaluationItem> evaluationItems) {
		return evaluationItems.stream()
			.map(this::toEvaluationItemCategoryDto)
			.toList();
	}

	public EvaluationItemCategoryDto toEvaluationItemCategoryDto(EvaluationItem evaluationItem) {
		return new EvaluationItemCategoryDto(evaluationItem.getId(), evaluationItem.getCategory());
	}

	public EvaluationItemDetailDto toEvaluationItemDetailDto(EvaluationItemDto evaluationItemDto, List<TaskTypeDto> taskTypeDto) {
		return EvaluationItemDetailDto.builder()
			.evaluationItemId(evaluationItemDto.getEvaluationItemId())
			.category(evaluationItemDto.getCategory())
			.weight(evaluationItemDto.getWeight())
			.period(evaluationItemDto.getPeriod())
			.purpose(evaluationItemDto.getPurpose())
			.isAuto(true)
			.formula(evaluationItemDto.getFormula())
			.unit(evaluationItemDto.getUnit())
			.serviceTargets(evaluationItemDto.getServiceTargets())
			.taskTypes(taskTypeDto)
			.build();
	}

	public EvaluationItem toEntity(CreateEvaluationItemDto createEvaluationItemDto, Contract contract) {
		return EvaluationItem.builder()
			.contract(contract)
			.category(createEvaluationItemDto.getCategory())
			.weight(createEvaluationItemDto.getWeight())
			.period(createEvaluationItemDto.getPeriod())
			.purpose(createEvaluationItemDto.getPurpose())
			.formula(createEvaluationItemDto.getFormula())
			.unit(createEvaluationItemDto.getUnit())
			.isActive(true)
			.build();
	}
}
