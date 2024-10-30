package project.slash.evaluationitem.repository;

import java.util.List;
import java.util.Optional;

import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.dto.response.EvaluationItemDto;

public interface EvaluationItemRepositoryCustom {
	List<EvaluationItemDto> findEvaluationItemInfos(Long contractId);
	//
	// Optional<EvaluationItemDetailDto> findEvaluationItemDetail(Long categoryId);
}
