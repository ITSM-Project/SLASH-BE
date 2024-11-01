package project.slash.statistics.repository;

import java.util.List;

import project.slash.statistics.dto.MonthlyDataDto;

public interface StatisticsRepositoryCustom {
	List<MonthlyDataDto> getMonthlyData();
}
