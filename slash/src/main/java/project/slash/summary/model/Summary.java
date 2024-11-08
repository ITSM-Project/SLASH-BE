package project.slash.summary.model;

import java.sql.Date;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "slash")
@Data
public class Summary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "statistics_id", nullable = false)
	private Long statisticsId;

	@Column(nullable = false)
	private Date date;

	@Column(nullable = false)
	private String grade;

	@Column(nullable = false)
	private double score;

	@Column(nullable = false)
	private String period;

	@Column(name = "evaluation_item_id", nullable = false)
	private Long evaluationItemId;

	@Column(name = "weighted_score", nullable = false)
	private double weightedScore;

	@Column(name = "approval_status", nullable = false)
	private boolean approvalStatus;

	@Column(nullable = false)
	private double estimate;

	@Column(name = "total_downtime")
	private Long totalDowntime;

	@Column(name = "request_count", nullable = false)
	private Long requestCount;

	@Column(name = "due_on_time_count")
	private Long dueOnTimeCount;

	@Column(name = "target_system")
	private String targetSystem;

	@Column(name = "service_type")
	private String serviceType;

	@Column(name = "target_equipment")
	private String targetEquipment;

	@Column(name = "is_auto", nullable = false)
	private boolean isAuto;

}
