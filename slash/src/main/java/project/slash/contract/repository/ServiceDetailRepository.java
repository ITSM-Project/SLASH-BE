package project.slash.contract.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.contract.model.ServiceDetail;

public interface ServiceDetailRepository extends JpaRepository<ServiceDetail, Long> {
}
