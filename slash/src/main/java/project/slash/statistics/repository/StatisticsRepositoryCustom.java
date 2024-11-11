package project.slash.statistics.repository;

import java.util.List;

import project.slash.statistics.dto.response.MonthlyDataDto;
import project.slash.statistics.dto.response.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.response.MonthlyStatisticsDto;
import project.slash.statistics.dto.response.StatisticsDto;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.StatisticsDto;
import project.slash.statistics.dto.response.ResponseServiceTaskDto;

public interface StatisticsRepositoryCustom {
	List<MonthlyDataDto> getMonthlyData(LocalDate date);

	void saveMonthlyData(List<MonthlyStatisticsDto> monthlyStatisticsDtoList);

	List<StatisticsDto> getStatistics(String serviceType, String period, String targetSystem, String targetEquipment);

	ResponseServiceTaskDto getServiceTaskStatics(Long evaluationItemId, LocalDate date);
}
