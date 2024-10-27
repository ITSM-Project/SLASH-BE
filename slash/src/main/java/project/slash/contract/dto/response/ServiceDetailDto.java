package project.slash.contract.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.slash.contract.dto.GradeDto;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServiceDetailDto {
	private String period;

	private String purpose;

	private String formula;

	private List<GradeDto> serviceTargets;
}
