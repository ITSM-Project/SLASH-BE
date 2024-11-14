package project.slash.statistics.mapper;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import project.slash.contract.model.EvaluationItem;
import project.slash.statistics.dto.response.CalculatedStatisticsDto;
import project.slash.statistics.dto.response.IndicatorDto;
import project.slash.statistics.dto.response.MonthlyServiceStatisticsDto;
import project.slash.statistics.dto.response.ResponseServiceTaskDto;
import project.slash.statistics.dto.response.ResponseStatisticsDto;
import project.slash.statistics.model.Statistics;

@Component
public class StatisticsMapper {

	public CalculatedStatisticsDto toCalculatedStatistics(Statistics statistics) {
		return new CalculatedStatisticsDto(
			statistics.getId(),
			statistics.getEvaluationItem().getId(),
			statistics.getServiceType(),
			statistics.isAuto(),
			statistics.getDate(),
			statistics.getCalculateTime(),
			statistics.isApprovalStatus()
		);
	}

	public List<MonthlyServiceStatisticsDto> toCalculatedStatisticsDtos(List<Statistics> statistics) {
		return statistics.stream()
			.map(this::toMonthlyServiceStatisticsDto)
			.toList();
	}

	public MonthlyServiceStatisticsDto toMonthlyServiceStatisticsDto(Statistics statistics) {
		return MonthlyServiceStatisticsDto.builder()
			.statisticsId(statistics.getId())
			.date(statistics.getDate())
			.calculateTime(statistics.getCalculateTime())
			.serviceType(statistics.getServiceType())
			.targetEquipment(statistics.getTargetEquipment())
			.grade(statistics.getGrade())
			.score(statistics.getScore())
			.period(statistics.getPeriod())
			.weightedScore(statistics.getWeightedScore())
			.approvalStatus(statistics.isApprovalStatus())
			.totalDowntime(statistics.getTotalDowntime())
			.requestCount(statistics.getRequestCount())
			.targetSystem(statistics.getTargetSystem())
			.estimate(statistics.getEstimate())
			.systemIncidentCount(statistics.getSystemIncidentCount())
			.dueOnTimeCount(statistics.getDueOnTimeCount())
			.build();
	}

	public List<IndicatorDto> toMonthlyIndicators(List<Statistics> statistics) {
		return statistics.stream()
			.map(this::toIndicatorDto)
			.toList();
	}

	public Statistics toEntityFromResponseServiceTask(ResponseServiceTaskDto responseServiceTaskDto, LocalDate endDate,
		double score, double weightedScore, String grade) {
		return Statistics.builder()
			.date(endDate)
			.serviceType(responseServiceTaskDto.getEvaluationItem().getCategory())
			.targetSystem("전체")
			.targetEquipment("전체")
			.grade(grade)
			.score(score)
			.period(responseServiceTaskDto.getEvaluationItem().getPeriod())
			.weightedScore(weightedScore)
			.requestCount(responseServiceTaskDto.getTaskRequest())
			.approvalStatus(false)
			.dueOnTimeCount(responseServiceTaskDto.getDueOnTimeCount())
			.estimate(score)
			.evaluationItem(responseServiceTaskDto.getEvaluationItem())
			.totalDowntime(-1)
			.systemIncidentCount(-1)
			.isAuto(false)
			.build();
	}

	public Statistics toEntityFromResponseStatisticsDto(ResponseStatisticsDto responseStatisticsDto,
		EvaluationItem evaluationItem) {
		return Statistics.builder()
			.date(responseStatisticsDto.getDate())
			.serviceType(responseStatisticsDto.getServiceType())
			.targetSystem(responseStatisticsDto.getTargetSystem())
			.targetEquipment(responseStatisticsDto.getTargetEquipment())
			.grade(responseStatisticsDto.getGrade())
			.score(responseStatisticsDto.getScore())
			.period(responseStatisticsDto.getPeriod())
			.weightedScore(responseStatisticsDto.getWeightedScore())
			.requestCount(responseStatisticsDto.getRequestCount())
			.approvalStatus(false)
			.dueOnTimeCount(responseStatisticsDto.getDueOnTimeCount())
			.estimate(responseStatisticsDto.getEstimate())
			.evaluationItem(evaluationItem)
			.totalDowntime(-1)
			.systemIncidentCount(-1)
			.isAuto(false)
			.build();
	}

	public IndicatorDto toIndicatorDto(Statistics statistics) {
		return new IndicatorDto(
			statistics.getEvaluationItem().getId(),
			statistics.getEvaluationItem().getCategory(),
			statistics.getDate(),
			statistics.getCalculateTime(),
			statistics.isAuto(),
			statistics.getGrade(),
			statistics.getScore(),
			statistics.getTargetSystem()
		);
	}
}


