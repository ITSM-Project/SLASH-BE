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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.GradeDto;

@Entity
@Table(name = "service_target")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

	public static ServiceTarget from(GradeDto serviceTarget, EvaluationItem evaluationItem) {
		return ServiceTarget.builder()
			.grade(serviceTarget.getGrade())
			.min(serviceTarget.getMin())
			.minInclusive(serviceTarget.getMinInclusive())
			.score(serviceTarget.getScore())
			.max(serviceTarget.getMax())
			.maxInclusive(serviceTarget.getMaxInclusive())
			.evaluationItem(evaluationItem)
			.build();
	}
}
