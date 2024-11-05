package project.slash.statistics.repository;

import java.util.List;

import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatsDto;
import project.slash.statistics.dto.StatsDto;

public interface StatisticsRepositoryCustom {
	List<MonthlyDataDto> getMonthlyData();

	void saveMonthlyData(List<MonthlyServiceStatsDto> monthlyServiceStatsDtoList);

	List<StatsDto> getStatistics(String serviceType, String period, String targetSystem, String targetEquipment);
}
