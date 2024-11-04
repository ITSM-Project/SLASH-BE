package project.slash.statistics.repository;

import java.util.List;

import com.querydsl.core.Tuple;

import project.slash.statistics.dto.MonthlyDataDto;
import project.slash.statistics.dto.MonthlyServiceStatsDto;

public interface StatisticsRepositoryCustom {
	List<MonthlyDataDto> getMonthlyData();

	void saveMonthlyData(List<MonthlyServiceStatsDto> monthlyServiceStatsDtoList);

	Tuple getIncidentsCount();

}
