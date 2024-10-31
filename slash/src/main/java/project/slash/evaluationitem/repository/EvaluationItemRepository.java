package project.slash.evaluationitem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.evaluationitem.model.EvaluationItem;

public interface EvaluationItemRepository extends JpaRepository<EvaluationItem, Long>, EvaluationItemRepositoryCustom {
}
