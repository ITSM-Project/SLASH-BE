package project.slash.statistics.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseStatisticsDto {
	private String targetSystem;
	private String serviceType;
	private String targetEquipment;
	private String grade;
	private double score;
	private String period;
	private double weightedScore;
	private long totalDowntime;
	private long requestCount;
	private long dueOnTimeCount;
	private double estimate;
	private long systemIncidentCount;
	private Long evaluationItemId;

	public static ResponseStatisticsDto fromResponseServiceTask(ResponseServiceTaskDto responseServiceTaskDto,
		double score, double weightedScore, String grade) {
		return ResponseStatisticsDto.builder()
			.serviceType(responseServiceTaskDto.getEvaluationItem().getCategory())
			.targetSystem("전체")
			.targetEquipment("전체")
			.grade(grade)
			.score(score)
			.period(responseServiceTaskDto.getEvaluationItem().getPeriod())
			.weightedScore(weightedScore)
			.requestCount(responseServiceTaskDto.getTaskRequest())
			.dueOnTimeCount(responseServiceTaskDto.getDueOnTimeCount())
			.estimate(score)
			.totalDowntime(-1)
			.systemIncidentCount(-1)
			.evaluationItemId(responseServiceTaskDto.getEvaluationItem().getId())
			.build();
	}
}
