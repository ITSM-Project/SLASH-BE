package project.slash.taskrequest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import project.slash.taskrequest.model.TaskRequest;

public interface TaskRequestRepository extends JpaRepository<TaskRequest, Long> {
	Optional<TaskRequest> findDetailById(Long requestId);
}
