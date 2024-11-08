package project.slash.statistics.dto.request;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class RequestStatisticsDto {

	private Long evaluationItemId;
	private LocalDate date;
}
