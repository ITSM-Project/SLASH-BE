package project.slash.contract.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.contract.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
	List<Contract> findAllByOrderByStartDateDesc();

	Optional<Contract> findByIsTerminateFalse();
}
