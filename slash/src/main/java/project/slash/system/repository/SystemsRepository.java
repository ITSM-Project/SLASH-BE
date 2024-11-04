package project.slash.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.slash.system.model.Systems;

public interface SystemsRepository extends JpaRepository<Systems, Long>, SystemsRepositoryCustom {
	List<Systems> findDistinctByNameNotNull();
}
