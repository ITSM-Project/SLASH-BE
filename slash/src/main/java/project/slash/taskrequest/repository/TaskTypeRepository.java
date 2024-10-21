package project.slash.taskrequest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.slash.taskrequest.model.TaskType;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {
	@Query("select tt.taskDetail from TaskType tt where (:taskType is null or tt.taskType = :taskType)")
	List<String> findAllByTaskType(@Param("taskType") String taskType);
}
