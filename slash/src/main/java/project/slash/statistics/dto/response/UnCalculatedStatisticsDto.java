package project.slash.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnCalculatedStatisticsDto {
	private Long evaluationItemId;
	private String category;
}
