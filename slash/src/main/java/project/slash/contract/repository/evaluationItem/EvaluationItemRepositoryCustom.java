package project.slash.contract.repository.evaluationItem;

import java.util.List;

import project.slash.contract.dto.response.EvaluationItemDto;

public interface EvaluationItemRepositoryCustom {
	List<EvaluationItemDto> findEvaluationItemInfos(Long contractId);
}