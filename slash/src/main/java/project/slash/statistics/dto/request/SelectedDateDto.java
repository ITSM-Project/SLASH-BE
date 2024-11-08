package project.slash.statistics.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SelectedDateDto {
	LocalDate date;
	long evaluationItemId;
}
