package project.slash.statistics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import project.slash.contract.model.EvaluationItem;

import java.time.LocalDate;

@Entity
public class Statistics {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "statistics_id")
	private Long id;
	private LocalDate date;
	@Column(name = "target_system")
	private String targetSystem;
	@Column(name = "service_type")
	private String serviceType;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_item_id")
	private EvaluationItem evaluationItems;
}
