package project.slash.contract.dto.request;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class CreateContractDto {
	private String companyName;
	private LocalDate startDate;
	private LocalDate endDate;
}
