package project.slash.serviceassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.serviceassessment.model.ServiceTarget;

public interface ServiceTargetRepository extends JpaRepository<ServiceTarget, Long> {
}
