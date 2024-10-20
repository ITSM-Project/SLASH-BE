package project.slash.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.system.model.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
