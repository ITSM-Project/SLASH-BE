package project.slash.taskrequest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.slash.taskrequest.model.TaskType;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {
	@Query("select tt.taskDetail from TaskType tt where (:taskType is null or tt.taskType = :taskType)")
	List<String> findAllByTaskType(@Param("taskType") String taskType);

	@Query("select tt from TaskType tt where tt.taskType = :taskType and tt.taskDetail = :taskDetail and tt.serviceRelevance = :serviceRelevance")
	Optional<TaskType> findTaskTypeByTaskRequestInfo(@Param("taskType") String taskType,
		@Param("taskDetail") String taskDetail,
		@Param("serviceRelevance") Boolean serviceRelevance);
}
