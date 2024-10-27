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

@Entity
@Table(name = "total_target")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id")
	private Contract contract;

	void setContract(Contract contract) {
		this.contract = contract;
		if (!contract.getTotalTargets().contains(this)) {
			contract.addTotalTarget(this);
		}
	}
}