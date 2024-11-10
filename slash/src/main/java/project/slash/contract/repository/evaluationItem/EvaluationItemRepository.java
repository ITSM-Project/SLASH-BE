package project.slash.contract.repository.evaluationItem;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.slash.contract.model.EvaluationItem;

public interface EvaluationItemRepository extends JpaRepository<EvaluationItem, Long>, EvaluationItemRepositoryCustom {
	@Query("select e.id from EvaluationItem e WHERE e.contract.id = :contractId")
	List<Long> findIdsByContractId(@Param("contractId") Long contractId);

	List<EvaluationItem> findByContractIdAndActiveIsTrue(Long contractId);
}
