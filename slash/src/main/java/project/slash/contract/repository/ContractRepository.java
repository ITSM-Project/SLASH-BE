package project.slash.contract.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.contract.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
}
