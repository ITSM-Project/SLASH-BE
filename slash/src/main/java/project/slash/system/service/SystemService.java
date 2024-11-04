package project.slash.system.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.slash.system.dto.response.AllSystemsInfo;
import project.slash.system.model.Systems;
import project.slash.system.repository.SystemsRepository;

@Service
@RequiredArgsConstructor
public class SystemService {
	private final SystemsRepository systemsRepository;
	public List<AllSystemsInfo> showAllSystems() {
		return systemsRepository.findAllSystemsWithEquipments();
	}

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
}
