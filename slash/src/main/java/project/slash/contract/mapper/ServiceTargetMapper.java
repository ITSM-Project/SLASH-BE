package project.slash.contract.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.ServiceTarget;

@Component
public class ServiceTargetMapper {
	public List<ServiceTarget> toServiceTargetList(List<GradeDto> serviceTargetDtos, EvaluationItem evaluationItem) {
		return serviceTargetDtos.stream()
			.map(serviceTargetDto -> ServiceTarget.from(serviceTargetDto, evaluationItem))
			.toList();
	}
}
