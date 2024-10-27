package project.slash.contract.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Contract {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contract_id")
	private Long id;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "is_terminate")
	private boolean isTerminate;

	@Column(name = "company_name")
	private String companyName;

	@OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TotalTarget> totalTargets = new ArrayList<>();

	@OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EvaluationItem> evaluationItems = new ArrayList<>();

	// 연관관계 편의 메서드
	public void addTotalTarget(TotalTarget totalTarget) {
		this.totalTargets.add(totalTarget);
		totalTarget.setContract(this);
	}

	public void addEvaluationItem(EvaluationItem evaluationItem) {
		this.evaluationItems.add(evaluationItem);
		evaluationItem.setContract(this);
	}
}
