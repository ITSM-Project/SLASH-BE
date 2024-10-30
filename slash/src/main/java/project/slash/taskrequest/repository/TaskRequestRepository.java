package project.slash.taskrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.slash.taskrequest.model.TaskRequest;

public interface TaskRequestRepository extends JpaRepository<TaskRequest, Long> {
}
