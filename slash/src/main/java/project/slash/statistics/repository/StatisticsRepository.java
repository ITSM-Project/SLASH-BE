package project.slash.statistics.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.statistics.model.Statistics;

public interface StatisticsRepository extends JpaRepository<Statistics, Long>, StatisticsRepositoryCustom {
	List<Statistics> findByDateAndEvaluationItemContractId(LocalDate date, Long contractId);

	List<Statistics> findByEvaluationItem_IdIn(List<Long> evaluationItemIds);

	Optional<Statistics> findByEvaluationItemIdAndApprovalStatusTrueAndDateBetween(Long evaluationItemId, LocalDate startDate, LocalDate endDate);

	List<Statistics> findByEvaluationItemIdAndDate(Long evaluationItem_id, LocalDate date);

	List<Statistics> findByDateAndEvaluationItemContractIdAndApprovalStatusTrueAndTargetSystem(LocalDate date, Long contractId, String targetSystem);
}
