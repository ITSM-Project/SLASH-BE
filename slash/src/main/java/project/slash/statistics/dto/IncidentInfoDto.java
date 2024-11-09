package project.slash.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncidentInfoDto {
	private long totalIncidentCount;
	private long totalOverdueCount;
}
