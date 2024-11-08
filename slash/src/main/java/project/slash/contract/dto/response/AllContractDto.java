package project.slash.contract.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.slash.contract.model.Contract;

@Getter
@AllArgsConstructor
public class AllContractDto {
	private Long contractId;

	private String companyName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private boolean isTerminate;

	public static AllContractDto from(Contract contract) {
		return new AllContractDto(contract.getId(), contract.getContractName(), contract.getStartDate(),
			contract.getEndDate(), contract.isTerminate());
	}
}
