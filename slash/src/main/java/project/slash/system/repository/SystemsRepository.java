package project.slash.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.system.model.Systems;

public interface SystemsRepository extends JpaRepository<Systems, Long> {
}
