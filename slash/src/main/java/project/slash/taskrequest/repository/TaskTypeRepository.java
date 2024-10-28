package project.slash.taskrequest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.slash.taskrequest.model.TaskType;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long>, TaskTypeRepositoryCustom {
	@Query("select tt from TaskType tt where tt.type = :type and tt.taskDetail = :taskDetail and tt.serviceRelevance = :serviceRelevance")
	Optional<TaskType> findTaskTypeByTaskRequestInfo(@Param("type") String type,
		@Param("taskDetail") String taskDetail,
		@Param("serviceRelevance") boolean serviceRelevance);
}
