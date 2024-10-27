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

	private Contract(LocalDate startDate, LocalDate endDate, String companyName, List<TotalTarget> totalTargets) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.companyName = companyName;
		this.totalTargets = totalTargets;
	}

	public static Contract from(CreateContractDto createContractDto) {
		List<TotalTarget> totalTargets = createContractDto.getTotalTargets().stream()
			.map(TotalTarget::from)
			.toList();

		Contract contract = new Contract(createContractDto.getStartDate(), createContractDto.getEndDate(),
			createContractDto.getCompanyName(), totalTargets);

		contract.addTotalTargets();

		return contract;
	}

	private void addTotalTargets() {
		totalTargets.forEach(target -> target.setContract(this));
	}
}
