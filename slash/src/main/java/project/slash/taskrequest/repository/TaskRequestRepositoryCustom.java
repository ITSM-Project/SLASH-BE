package project.slash.taskrequest.repository;

import java.util.List;

import project.slash.taskrequest.dto.response.StatusCountDto;
import project.slash.taskrequest.dto.response.SystemCountDto;
import project.slash.taskrequest.dto.response.TaskTypeCountDto;

public interface TaskRequestRepositoryCustom {
	List<StatusCountDto> findCountByStatus(int year, int month, String user);

	List<TaskTypeCountDto> findCountByTaskType(int year, int month, String user);

	List<SystemCountDto> findCountBySystem(int year, int month, String user);
}
