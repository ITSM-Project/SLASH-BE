package project.slash.evaluationitem.repository;

import java.util.List;
import java.util.Optional;

import project.slash.contract.dto.response.PreviewEvaluationItemDto;
import project.slash.evaluationitem.dto.response.EvaluationItemDto;

public interface EvaluationItemRepositoryCustom {
	List<PreviewEvaluationItemDto> findEvaluationItem(Long contractId);

	Optional<EvaluationItemDto> findEvaluationItemDetail(Long evaluationItemId);
}
