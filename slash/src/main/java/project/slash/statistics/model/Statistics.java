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
import lombok.Getter;

import java.time.LocalDate;

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
	private LocalDate calculateTime;

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

	public void approve() {
		this.approvalStatus = true;
	}

	public void update(String grade, double score, double weightedScore) {
		this.grade = grade;
		this.score = score;
		this.weightedScore = weightedScore;
	}
}
