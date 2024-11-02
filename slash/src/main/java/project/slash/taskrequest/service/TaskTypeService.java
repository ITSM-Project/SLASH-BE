package project.slash.taskrequest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.slash.taskrequest.dto.response.AllTaskTypeDto;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
public class TaskTypeService {
	private final TaskTypeRepository taskTypeRepository;

	public List<AllTaskTypeDto> allTaskTypes() {
		return taskTypeRepository.findAllTaskTypes();
	}
}
