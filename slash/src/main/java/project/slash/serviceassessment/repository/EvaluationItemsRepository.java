package project.slash.serviceassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.serviceassessment.model.EvaluationItems;

public interface EvaluationItemsRepository extends JpaRepository<EvaluationItems, Long> {
}
