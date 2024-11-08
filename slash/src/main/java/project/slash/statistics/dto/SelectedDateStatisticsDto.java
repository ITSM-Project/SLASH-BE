package project.slash.statistics.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SelectedDateStatisticsDto {
	LocalDate date;
	long evaluationItemId;
}
