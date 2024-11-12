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
			.map(serviceTargetDto -> toEntity(serviceTargetDto, evaluationItem))
			.toList();
	}

	public ServiceTarget toEntity(GradeDto serviceTarget, EvaluationItem evaluationItem) {
		return ServiceTarget.builder()
			.grade(serviceTarget.getGrade())
			.min(serviceTarget.getMin())
			.minInclusive(serviceTarget.getMinInclusive())
			.score(serviceTarget.getScore())
			.max(serviceTarget.getMax())
			.maxInclusive(serviceTarget.getMaxInclusive())
			.evaluationItem(evaluationItem)
			.build();
	}
}
