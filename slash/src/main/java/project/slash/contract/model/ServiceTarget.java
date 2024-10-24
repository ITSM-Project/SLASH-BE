package project.slash.contract.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "service_target")
@Getter
public class ServiceTarget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "target_id")
	private Long id;

	private String grade;

	private double min;

	private double max;

	private double score;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_item_id")
	private EvaluationItems evaluationItems;

	void setEvaluationItems(EvaluationItems evaluationItems) {
		this.evaluationItems = evaluationItems;
	}
}
