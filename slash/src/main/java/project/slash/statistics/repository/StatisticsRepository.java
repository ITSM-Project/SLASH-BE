package project.slash.statistics.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.statistics.model.Statistics;

public interface StatisticsRepository extends JpaRepository<Statistics, Long>, StatisticsRepositoryCustom {
	List<Statistics> findByDateBetweenAndEvaluationItemContractId(LocalDate startDate, LocalDate endDate, Long contractId);

	List<Statistics> findByEvaluationItem_IdIn(List<Long> evaluationItemIds);

	Optional<Statistics> findByEvaluationItemIdAndApprovalStatusTrueAndDateBetween(Long evaluationItemId, LocalDate startDate, LocalDate endDate);

	List<Statistics> findByEvaluationItemIdAndCalculateTime(Long evaluationItem_id, LocalDate calculateTime);

	List<Statistics> findByDateBetweenAndEvaluationItemContractIdAndApprovalStatusTrueAndTargetSystem(LocalDate startDate, LocalDate endDate, Long contractId, String targetSystem);
}
