package project.slash.evaluationitem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.evaluationitem.model.ServiceTarget;

public interface ServiceTargetRepository extends JpaRepository<ServiceTarget, Long> {
}
