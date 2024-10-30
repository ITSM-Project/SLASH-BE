package project.slash.contract.repository.evaluationItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.contract.model.EvaluationItem;

public interface EvaluationItemRepository extends JpaRepository<EvaluationItem, Long>, EvaluationItemRepositoryCustom {
}
