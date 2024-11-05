package project.slash.statistics.repository;

import java.util.List;

import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.StatisticsDto;

public interface StatisticsRepositoryCustom {
	List<MonthlyDataDto> getMonthlyData();

	void saveMonthlyData(List<MonthlyServiceStatisticsDto> monthlyServiceStatisticsDtoList);

	List<StatisticsDto> getStatistics(String serviceType, String period, String targetSystem, String targetEquipment);
}
