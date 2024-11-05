package project.slash.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.statistics.model.Statistics;

public interface StatisticsRepository extends JpaRepository<Statistics, Long>, StatisticsRepositoryCustom {
}
