package project.slash.contract.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.model.Contract;
import project.slash.contract.model.TotalTarget;

@Component
public class TotalTargetMapper {
	public List<TotalTarget> toTotalTargetList(List<GradeDto> totalTargets, Contract contract) {
		return totalTargets.stream()
			.map(target -> toEntity(target, contract))
			.toList();
	}

	public List<GradeDto> toGradeDtoList(List<TotalTarget> totalTargets) {
		return totalTargets.stream()
			.map(this::toGradeDto)
			.toList();
	}

	public GradeDto toGradeDto(TotalTarget target){
		return GradeDto.builder()
			.grade(target.getGrade())
			.min(target.getMin())
			.max(target.getMax())
			.minInclusive(target.isMinInclusive())
			.maxInclusive(target.isMaxInclusive())
			.build();
	}

	public TotalTarget toEntity(GradeDto gradeDto, Contract contract) {
		return TotalTarget.builder()
			.contract(contract)
			.grade(gradeDto.getGrade())
			.min(gradeDto.getMin())
			.minInclusive(gradeDto.getMinInclusive())
			.max(gradeDto.getMax())
			.maxInclusive((gradeDto.getMaxInclusive()))
			.isActive(true)
			.build();
	}
}
