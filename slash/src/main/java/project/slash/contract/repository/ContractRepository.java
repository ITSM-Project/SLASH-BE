package project.slash.contract.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.slash.contract.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
	@Query("select c from Contract c where c.isTerminate = false")
	Contract findContractsNotTerminate();
}
