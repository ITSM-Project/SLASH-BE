package project.slash.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.slash.system.model.Systems;

@Repository
public interface SystemsRepository extends JpaRepository<Systems, Long>, SystemsRepositoryCustom {
}
