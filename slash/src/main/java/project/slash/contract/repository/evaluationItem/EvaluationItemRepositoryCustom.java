package project.slash.contract.repository.evaluationItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import project.slash.contract.dto.response.EvaluationItemDto;
import project.slash.contract.model.EvaluationItem;

public interface EvaluationItemRepositoryCustom {
	List<EvaluationItemDto> findAllEvaluationItems(Long contractId);

	Optional<EvaluationItemDto> findEvaluationItem(Long evaluationItemId);

	List<EvaluationItem> findUnCalculatedEvaluationItem(Long contractId, LocalDate beforeDate);
}
