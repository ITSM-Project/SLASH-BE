package project.slash.contract.repository;

import java.util.List;
import java.util.Optional;

import project.slash.contract.dto.response.EvaluationItemDto;

public interface EvaluationItemRepositoryCustom {
	List<EvaluationItemDto> findAllEvaluationItems(Long contractId);

	Optional<EvaluationItemDto> findEvaluationItemDetail(Long evaluationItemId);
}
