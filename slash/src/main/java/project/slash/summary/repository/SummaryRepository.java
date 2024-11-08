package project.slash.summary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.summary.model.Summary;

public interface SummaryRepository extends JpaRepository<Summary, Long>, SummaryRepositoryCustom {

}
