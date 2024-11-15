package project.slash.statistics.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import project.slash.statistics.dto.response.MonthlyDataDto;
import project.slash.statistics.dto.response.MonthlyStatisticsDto;
import project.slash.statistics.dto.response.ResponseServiceTaskDto;

public interface StatisticsRepositoryCustom {
	List<MonthlyDataDto> getMonthlyData(LocalDate date);

	void saveMonthlyData(List<MonthlyStatisticsDto> monthlyStatisticsDtoList);

	ResponseServiceTaskDto getServiceTaskStatics(Long evaluationItemId, LocalDateTime startDate, LocalDateTime endDate);
}
