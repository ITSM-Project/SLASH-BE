package project.slash.taskrequest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.slash.taskrequest.model.TaskType;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long>, TaskTypeRepositoryCustom {
	@Query("select t from TaskType t where t.taskDetail = :taskDetail and t.serviceRelevance = :serviceRelevance and t.evaluationItem.contract.id = :contractId")
	Optional<TaskType> findTaskTypeByTaskRequestInfo(
		@Param("taskDetail") String taskDetail,
		@Param("serviceRelevance") boolean serviceRelevance,
		@Param("contractId") Long contractId
	);

	List<TaskType> findTaskTypesByEvaluationItemId(Long categoryId);

	List<TaskType> findDistinctByTypeNotNull();

	List<TaskType> findDistinctByTaskDetailNotNull();
}
