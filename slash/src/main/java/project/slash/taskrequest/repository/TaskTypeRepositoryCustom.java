package project.slash.taskrequest.repository;

import java.util.List;

import project.slash.taskrequest.dto.response.AllTaskTypeDto;

public interface TaskTypeRepositoryCustom {
	List<AllTaskTypeDto> findAllTaskTypes(Long contractId);
}
