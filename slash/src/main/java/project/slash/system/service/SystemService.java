package project.slash.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import project.slash.system.dto.response.AllSystemsInfo;
import project.slash.system.model.Equipment;
import project.slash.system.model.Systems;
import project.slash.system.repository.EquipmentRepository;
import project.slash.system.repository.SystemsRepository;

@Service
@RequiredArgsConstructor
public class SystemService {
	private final SystemsRepository systemsRepository;
	private final EquipmentRepository equipmentRepository;

	@PostConstruct
	public void initSystemData() {
		if(systemsRepository.findAll().isEmpty()) {
			List<String> systemNames = List.of("DB", "백업", "서버", "응용프로그램");
			List<Systems> systems = systemNames.stream()
				.map(Systems::of)
				.toList();
			List<Systems> savedSystems = systemsRepository.saveAll(systems);

			for (Systems system : savedSystems) {
				List<Equipment> equipments = List.of(
					Equipment.from(system.getName() + "#1", system),
					Equipment.from(system.getName() + "#2", system),
					Equipment.from(system.getName() + "#3", system),
					Equipment.from(system.getName() + "#4", system),
					Equipment.from(system.getName() + "#5", system)
				);
				equipmentRepository.saveAll(equipments);
			}
		}
	}

	public List<AllSystemsInfo> showAllSystems() {
		return systemsRepository.findAllSystemsWithEquipments();
	}
}
