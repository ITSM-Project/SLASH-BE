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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.request.GradeDto;

@Entity
@Table(name = "service_target")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ServiceTarget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "target_id")
	private Long id;

	private String grade;

	private double min;

	@Column(name = "min_inclusive")
	private boolean minInclusive;

	private double max;

	@Column(name = "max_inclusive")
	private boolean maxInclusive;

	private double score;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_item_id")
	private EvaluationItem evaluationItem;

	@Builder
	private ServiceTarget(String grade, double min, boolean minInclusive, double max, boolean maxInclusive,
		double score) {
		this.grade = grade;
		this.min = min;
		this.minInclusive = minInclusive;
		this.max = max;
		this.maxInclusive = maxInclusive;
		this.score = score;
	}

	public static ServiceTarget from(GradeDto serviceTarget) {
		return ServiceTarget.builder()
			.grade(serviceTarget.getGrade())
			.min(serviceTarget.getMin())
			.minInclusive(serviceTarget.isMinInclusive())
			.max(serviceTarget.getMax())
			.maxInclusive(serviceTarget.isMaxInclusive())
			.build();
	}

	void setEvaluationItem(EvaluationItem evaluationItems) {
		this.evaluationItem = evaluationItems;
	}
}
