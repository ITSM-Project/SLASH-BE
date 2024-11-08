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

	@Column(name = "contract_name")
	private String contractName;

	@Builder
	private Contract(String contractName, LocalDate startDate, LocalDate endDate, boolean isTerminate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.contractName = contractName;
		this.isTerminate = isTerminate;
	}

	public void updateTerminateStatus() {
		this.isTerminate = true;
	}
}
