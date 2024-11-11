package project.slash.statistics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import project.slash.contract.model.EvaluationItem;
import project.slash.statistics.dto.IncidentInfoDto;
import lombok.Getter;

import project.slash.statistics.dto.response.ResponseServiceTaskDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Statistics {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "statistics_id")
	private Long id;

	private LocalDate date;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime calculateTime;

	@Column(name = "target_system")
	private String targetSystem;

	@Column(name = "service_type")
	private String serviceType;

	@Column(name = "target_equipment")
	private String targetEquipment;

	private String grade;

	private double score;

	private String period;

	@Column(name = "weighted_score")
	private double weightedScore;

	@Column(name = "approval_status")
	private boolean approvalStatus;

	@Column(name = "total_downtime")
	private long totalDowntime;

	@Column(name = "request_count")
	private long requestCount;

	@Column(name = "due_on_time_count")
	private long dueOnTimeCount;

	private double estimate;

	@Column(name = "system_incident_count")
	private long systemIncidentCount;

	@Column(name = "is_auto")
	private boolean isAuto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_item_id")
	private EvaluationItem evaluationItem;

	public static Statistics fromIncidentInfo(IncidentInfoDto incidentInfoDto, LocalDate date, double score,
		double weightedScore, String grade, double estimate, EvaluationItem evaluationItem) {
		return Statistics.builder()
			.targetSystem("전체")
			.targetEquipment("전체")
			.serviceType("장애 적기처리율")
			.date(date)
			.approvalStatus(false)
			.grade(grade)
			.score(score)
			.period("월별")
			.totalDowntime(0)
			.weightedScore(weightedScore)
			.requestCount(incidentInfoDto.getTotalIncidentCount())
			.systemIncidentCount(incidentInfoDto.getTotalIncidentCount())
			.dueOnTimeCount(incidentInfoDto.getTotalIncidentCount() - incidentInfoDto.getTotalOverdueCount())
			.estimate(estimate)
			.evaluationItem(evaluationItem)
			.isAuto(false)
			.build();
	}

	public static Statistics fromResponseServiceTask(ResponseServiceTaskDto responseServiceTaskDto, LocalDate endDate,
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

	public void approve() {
		this.approvalStatus = true;
	}

	public void update(String grade, double score, double weightedScore) {
		this.grade = grade;
		this.score = score;
		this.weightedScore = weightedScore;
	}
}
