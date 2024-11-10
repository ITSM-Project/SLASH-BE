package project.slash.statistics.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.statistics.model.Statistics;

public interface StatisticsRepository extends JpaRepository<Statistics, Long>, StatisticsRepositoryCustom {
	List<Statistics> findByDateBetweenAndEvaluationItemsContractIdAndApprovalStatusTrue(LocalDate startDate, LocalDate endDate, Long contractId);

	List<Statistics> findByDateBetweenAndEvaluationItemsContractId(LocalDate startDate, LocalDate endDate, Long contractId);

	List<Statistics> findByEvaluationItems_IdIn(List<Long> evaluationItemIds);
}
