package project.slash.taskrequest.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import project.slash.system.model.Systems;
import project.slash.system.repository.SystemsRepository;
import project.slash.taskrequest.model.TaskType;
import project.slash.taskrequest.repository.TaskTypeRepository;

@Service
@RequiredArgsConstructor
public class OptionsService {
	private final SystemsRepository systemsRepository;
	private final TaskTypeRepository taskTypeRepository;

	public List<String> getDistinctSystemNames() {
		return Stream.concat(
				Stream.of("전체"),
				systemsRepository.findDistinctByNameNotNull()
					.stream()
					.map(Systems::getName)
					.distinct()
			)
			.collect(Collectors.toList());
	}

	public List<String> getDistinctTaskTypes() {
		return Stream.concat(
				Stream.of("전체"),
				taskTypeRepository.findDistinctByTypeNotNull()
					.stream()
					.map(TaskType::getType)
					.distinct()
			)
			.collect(Collectors.toList());
	}

	public List<String> getDistinctTaskDetails() {
		return Stream.concat(
				Stream.of("전체"),
				taskTypeRepository.findDistinctByTaskDetailNotNull()
					.stream()
					.map(TaskType::getTaskDetail)
					.distinct()
			)
			.collect(Collectors.toList());
	}
}
