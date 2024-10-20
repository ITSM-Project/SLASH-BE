package project.slash.taskrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.taskrequest.model.TaskType;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {
}
