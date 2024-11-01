package project.slash.taskrequest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.slash.taskrequest.dto.request.TaskResponseRequestDTO;
import project.slash.taskrequest.model.constant.RequestStatus;

public interface TaskRequestRepositoryCustom {
	Page<TaskResponseRequestDTO> findFilteredRequests(String equipmentName, String type, String taskDetail, RequestStatus status,Pageable pageable);
}
