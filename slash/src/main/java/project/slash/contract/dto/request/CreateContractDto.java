package project.slash.contract.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateContractDto {
	private String companyName;
	private LocalDate startDate;
	private LocalDate endDate;

	private List<EvaluationItemDto> evaluationItems;
}
