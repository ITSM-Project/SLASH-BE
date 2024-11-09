package project.slash.statistics.repository;

import java.time.LocalDate;
import java.util.List;

import project.slash.statistics.dto.response.MonthlyDataDto;
import project.slash.statistics.dto.response.MonthlyStatisticsDto;

public interface StatisticsRepositoryCustom {
	List<MonthlyDataDto> getMonthlyData(LocalDate date);

	void saveMonthlyData(List<MonthlyStatisticsDto> monthlyStatisticsDtoList);
}
