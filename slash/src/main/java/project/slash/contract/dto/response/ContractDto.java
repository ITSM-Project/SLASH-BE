package project.slash.contract.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.GradeDto;

@Getter 
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractDto {
	private String companyName;

	private LocalDate startDate;

	private LocalDate endDate;

	private List<String> categories;

	private List<GradeDto> totalTargets;
}
