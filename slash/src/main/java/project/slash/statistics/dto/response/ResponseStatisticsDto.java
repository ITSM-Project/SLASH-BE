package project.slash.statistics.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.statistics.dto.IncidentInfoDto;

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
	private LocalDate date;

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

	public static ResponseStatisticsDto fromIncidentDto(IncidentInfoDto incidentInfoDto,
		LocalDate date, double score,
		double weightedScore, String grade, double estimate, Long evaluationItemId) {
		return ResponseStatisticsDto.builder()
			.targetSystem("전체")
			.targetEquipment("전체")
			.serviceType("장애 적기처리율")
			.date(date)
			.grade(grade)
			.score(score)
			.period("월별")
			.totalDowntime(-1)
			.weightedScore(weightedScore)
			.requestCount(incidentInfoDto.getTotalIncidentCount())
			.systemIncidentCount(incidentInfoDto.getTotalIncidentCount())
			.dueOnTimeCount(incidentInfoDto.getTotalIncidentCount() - incidentInfoDto.getTotalOverdueCount())
			.estimate(estimate)
			.evaluationItemId(evaluationItemId)
			.build();
	}
}
