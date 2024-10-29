package project.slash.taskrequest.repository;

import java.util.List;

import project.slash.taskrequest.dto.response.TaskTypeDto;

public interface TaskTypeRepositoryCustom {
	List<TaskTypeDto> findAllTaskTypes();
}
