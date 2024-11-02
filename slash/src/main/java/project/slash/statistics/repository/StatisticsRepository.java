package project.slash.statistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.statistics.dto.StatsDto;
import project.slash.statistics.model.Statistics;

public interface StatisticsRepository extends JpaRepository<Statistics, Long>, StatisticsRepositoryCustom {
	List<StatsDto> getStatisticsByServiceTypeAndPeriodAndTargetSystem(String serviceType, String period,
		String targetSystem);
}
