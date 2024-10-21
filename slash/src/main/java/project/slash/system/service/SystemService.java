package project.slash.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.slash.system.dto.response.AllSystemsInfo;
import project.slash.system.repository.SystemsRepository;

@Service
@RequiredArgsConstructor
public class SystemService {
	private final SystemsRepository systemsRepository;
	public List<AllSystemsInfo> showAllSystems() {
		return systemsRepository.findAllSystemsWithEquipments();
	}
}
