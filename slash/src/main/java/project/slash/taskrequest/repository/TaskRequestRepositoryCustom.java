package project.slash.taskrequest.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.slash.statistics.dto.IncidentInfoDto;
import project.slash.taskrequest.dto.request.RequestManagementDto;
import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.SystemCountDto;
import project.slash.taskrequest.dto.response.TaskRequestOfManagerDto;
import project.slash.taskrequest.dto.response.TaskTypeCountDto;
import project.slash.taskrequest.model.constant.RequestStatus;

public interface TaskRequestRepositoryCustom {
	List<StatusCountDto> findCountByStatus(int year, int month, String user);

	List<TaskTypeCountDto> findCountByTaskType(int year, int month, String user);

	List<SystemCountDto> findCountBySystem(int year, int month, String user);

	List<TaskRequestOfManagerDto> findTaskRequestOfManager();

	Page<RequestManagementDto> findFilteredRequests(String equipmentName, String type,
		String taskDetail, RequestStatus status, String keyword, Pageable pageable);

	void updateManagerByRequestId(Long requestId, String managerId);

	void updateDueOnTime(Long requestId, String managerId, RequestStatus requestStatus);

	IncidentInfoDto getIncidentCount(Long evaluationItemId, LocalDate endDate);

}
