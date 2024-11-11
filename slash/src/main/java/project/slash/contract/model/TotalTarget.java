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
@Getter
@Table(name = "total_target")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TotalTarget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "total_target_id")
	private Long id;

	private String grade;

	private double min;

	@Column(name = "min_inclusive")
	private boolean minInclusive;

	private double max;

	@Column(name = "max_inclusive")
	private boolean maxInclusive;

	@Column(name = "is_active")
	private boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id")
	private Contract contract;

	public static TotalTarget from(GradeDto gradeDto, Contract contract) {
		return TotalTarget.builder()
			.contract(contract)
			.grade(gradeDto.getGrade())
			.min(gradeDto.getMin())
			.minInclusive(gradeDto.getMinInclusive())
			.max(gradeDto.getMax())
			.maxInclusive((gradeDto.getMaxInclusive()))
			.isActive(true)
			.build();
	}

	public void deactivate() {
		this.isActive = false;
	}
}
