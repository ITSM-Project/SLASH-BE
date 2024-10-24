package project.slash.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.system.model.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
	Optional<Equipment> findByName(String equipmentName);
}
