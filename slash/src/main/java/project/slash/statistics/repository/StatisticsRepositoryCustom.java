package project.slash.statistics.repository;

import java.time.LocalDateTime;
import java.util.List;

import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.statistics.dto.response.ResponseServiceTaskDto;

public interface StatisticsRepositoryCustom {
	List<MonthlyDataDto> getMonthlyData();

	void saveMonthlyData(List<MonthlyServiceStatisticsDto> monthlyServiceStatisticsDtoList);

	List<StatisticsDto> getStatistics(String serviceType, String period, String targetSystem, String targetEquipment);

	ResponseServiceTaskDto getServiceTaskStatics(Long evaluationItemId, LocalDateTime startDate,LocalDateTime endDate);
}
