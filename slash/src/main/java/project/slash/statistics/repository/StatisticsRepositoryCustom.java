package project.slash.statistics.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import project.slash.statistics.dto.response.MonthlyDataDto;
import project.slash.statistics.dto.response.ResponseServiceTaskDto;
import project.slash.statistics.dto.response.ResponseStatisticsDto;

public interface StatisticsRepositoryCustom {
	List<MonthlyDataDto> getMonthlyData(LocalDate date, long contractId);

	void saveMonthlyData(List<ResponseStatisticsDto> monthlyUptimeStatisticsDtoList);

	ResponseServiceTaskDto getServiceTaskStatics(Long evaluationItemId, LocalDateTime startDate, LocalDateTime endDate);
}
