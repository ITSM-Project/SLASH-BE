package project.slash.system.repository;

import java.util.List;

import project.slash.system.dto.response.AllSystemsInfo;

public interface SystemsRepositoryCustom {
	List<AllSystemsInfo> findAllSystemsWithEquipments();
}
