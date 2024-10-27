package project.slash.contract.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import project.slash.contract.dto.ContractDto;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.model.Contract;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.TotalTarget;

@Mapper
public interface ContractMapper {
	ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);

	@Mapping(target = "evaluationItems", source = "categories")
	@Mapping(target = "totalTargets", source = "totalTargets")
	Contract toEntity(ContractDto contractDto);

	// GradeDto 리스트 -> TotalTarget 리스트 매핑
	List<TotalTarget> mapTotalTargets(List<GradeDto> totalTargets);

	// String 리스트 -> EvaluationItem 리스트 매핑
	@Mapping(target = "contract", ignore = true)
	@Mapping(target = "category", source = ".")
	EvaluationItem mapEvaluationItem(String category);

	// 매핑 후 연관 관계 설정
	@AfterMapping
	default void setRelationships(@MappingTarget Contract contract) {
		contract.getTotalTargets().forEach(contract::addTotalTarget);
		contract.getEvaluationItems().forEach(contract::addEvaluationItem);
	}
}
