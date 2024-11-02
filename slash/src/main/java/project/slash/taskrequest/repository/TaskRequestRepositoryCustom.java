package project.slash.taskrequest.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.slash.taskrequest.dto.request.TaskResponseRequestDTO;
import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.SystemCountDto;
import project.slash.taskrequest.dto.response.TaskTypeCountDto;
import project.slash.taskrequest.model.constant.RequestStatus;

public interface TaskRequestRepositoryCustom {
	List<StatusCountDto> findCountByStatus(int year, int month, String user);

	List<TaskTypeCountDto> findCountByTaskType(int year, int month, String user);

	List<SystemCountDto> findCountBySystem(int year, int month, String user);
	Page<TaskResponseRequestDTO> findFilteredRequests(String equipmentName, String type, String taskDetail, RequestStatus status,Pageable pageable);
}
