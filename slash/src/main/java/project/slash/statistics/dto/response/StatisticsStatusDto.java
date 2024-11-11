package project.slash.statistics.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatisticsStatusDto {
	List<UnCalculatedStatisticsDto> unCalculatedStatistics;
	List<CalculatedStatisticsDto> calculatedStatistics;
}
