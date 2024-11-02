package project.slash.contract.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.contract.model.TotalTarget;

public interface TotalTargetRepository extends JpaRepository<TotalTarget, Long> {
	List<TotalTarget> findByContractId(Long id);
}
