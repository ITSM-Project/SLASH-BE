package project.slash.summary.dto.evaluation.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IncidentResolvedRateDto {
	private long requestCount;
	private long totalDownTime;
	private long systemIncidentCount;
	private long dueOnTimeCount;
}
