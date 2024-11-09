package project.slash.contract.repository.evaluationItem;

import java.util.List;
import java.util.Optional;

import project.slash.contract.dto.response.EvaluationItemDto;

public interface EvaluationItemRepositoryCustom {
	List<EvaluationItemDto> findAllEvaluationItems(Long contractId);

	Optional<EvaluationItemDto> findEvaluationItem(Long evaluationItemId);

	Integer findTotalWeightByEvaluationItemId(Long evaluationItemId);
}
