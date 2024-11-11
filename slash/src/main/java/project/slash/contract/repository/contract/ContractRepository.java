package project.slash.contract.repository.contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.contract.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> , ContractRepositoryCustom {
	List<Contract> findAllByOrderByStartDateDesc();

	Optional<Contract> findByIsTerminateFalse();

	List<Contract> findByEndDate(LocalDate now);
}
