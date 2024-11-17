package project.slash.statistics.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.response.ContractDataDto;
import project.slash.statistics.dto.IncidentInfoDto;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
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
	private boolean approvalStatus;
	private boolean isAuto;

	public static ResponseStatisticsDto fromResponseServiceTask(ResponseServiceTaskDto responseServiceTaskDto,
		double score, double weightedScore, String grade, LocalDate date) {
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
			.date(date)
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

	public static ResponseStatisticsDto of(ContractDataDto contractDataDto,MonthlyDataDto monthlyDataDto,EvaluatedDto evaluatedDto,LocalDate date) {
		return ResponseStatisticsDto.builder()
			.date(date)
			.serviceType(contractDataDto.getCategory())
			.targetEquipment(monthlyDataDto.getEquipmentName())
			.grade(evaluatedDto.getGrade())
			.score(evaluatedDto.getScore())
			.period("월별")
			.weightedScore(evaluatedDto.getWeightedScore())
			.approvalStatus(false)
			.totalDowntime(monthlyDataDto.getTotalDownTime())
			.requestCount(monthlyDataDto.getRequestCount())
			.evaluationItemId(evaluatedDto.getEvaluationItemId())
			.targetSystem(monthlyDataDto.getSystemName())
			.estimate(evaluatedDto.getScore())
			.systemIncidentCount(monthlyDataDto.getSystemIncidentCount())
			.dueOnTimeCount(-1)
			.isAuto(true)
			.build();
	}

	public static ResponseStatisticsDto ofResponseStatisticsDto(EvaluatedDto evaluatedDto,ResponseStatisticsDto responseStatisticsDto,double averageScore,double averageWeightedScore,long totalDowntime,long totalRequestCount,long evaluationItemId,long totalSystemIncidentCount) {
		return ResponseStatisticsDto.builder()
			.date(responseStatisticsDto.getDate())
			.serviceType(responseStatisticsDto.getServiceType())
			.targetEquipment("전체")
			.grade(evaluatedDto.getGrade())
			.score(averageScore)
			.period("월별")
			.weightedScore(averageWeightedScore)
			.approvalStatus(false)
			.totalDowntime(totalDowntime)
			.requestCount(totalRequestCount)
			.evaluationItemId(evaluationItemId)
			.targetSystem("전체")
			.estimate(averageScore)
			.systemIncidentCount(totalSystemIncidentCount)
			.dueOnTimeCount(-1)
			.isAuto(true)
			.build();
	}


}
