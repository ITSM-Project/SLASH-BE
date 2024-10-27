package project.slash.contract.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.request.CreateContractDto;
import project.slash.contract.dto.request.GradeDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

	private Contract(LocalDate startDate, LocalDate endDate, String companyName) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.companyName = companyName;
	}

	public static Contract from(CreateContractDto createContractDto) {
		Contract contract = new Contract(createContractDto.getStartDate(), createContractDto.getEndDate(), createContractDto.getCompanyName());

		contract.addTotalTargets(createContractDto.getTotalTargets());
		contract.addEvaluationItems(createContractDto.getCategorys());

		return contract;
	}

	private void addTotalTargets(List<GradeDto> targets) {
		targets.stream()
			.map(TotalTarget::from)
			.forEach(this::addTotalTarget);
	}

	private void addTotalTarget(TotalTarget target) {
		totalTargets.add(target);
		target.setContract(this);
	}

	private void addEvaluationItems(List<String> categories) {
		categories.stream()
			.map(category -> EvaluationItem.of(category, this))
			.forEach(this::addEvaluationItem);
	}

	private void addEvaluationItem(EvaluationItem item) {
		evaluationItems.add(item);
		item.setContract(this);
	}
}
