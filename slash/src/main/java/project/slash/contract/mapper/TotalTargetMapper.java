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
			.map(target -> TotalTarget.from(target, contract))
			.toList();
	}

	public List<GradeDto> toGradeDtoList(List<TotalTarget> totalTargets) {
		return totalTargets.stream()
			.map(GradeDto::createOf)
			.toList();
	}
}
