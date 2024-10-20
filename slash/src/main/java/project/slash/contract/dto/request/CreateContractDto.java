package project.slash.contract.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateContractDto {
	private String companyName;
	private LocalDate startDate;
	private LocalDate endDate;
}
