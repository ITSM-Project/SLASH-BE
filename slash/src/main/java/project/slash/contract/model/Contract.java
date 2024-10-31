package project.slash.contract.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.request.CreateContractDto;

import java.time.LocalDate;

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

	@Builder
	private Contract(String companyName, LocalDate startDate, LocalDate endDate, boolean isTerminate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.companyName = companyName;
		this.isTerminate = isTerminate;
	}

	public static Contract from(CreateContractDto dto) {
		return Contract.builder()
			.companyName(dto.getCompanyName())
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.build();
	}

	public void updateTerminateStatus() {
		this.isTerminate = true;
	}
}
