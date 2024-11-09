package project.slash.contract.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.contract.model.ServiceTarget;

public interface ServiceTargetRepository extends JpaRepository<ServiceTarget, Long> {
	List<ServiceTarget> findByEvaluationItemId(Long evaluationItemId);
}
