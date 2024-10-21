package project.slash.contract.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.contract.model.EvaluationItems;

public interface EvaluationItemsRepository extends JpaRepository<EvaluationItems, Long> {
}
