package project.slash.contract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.model.ServiceTarget;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServiceTargetDto {
	@NotBlank(message = "등급은 필수입니다.")
	private String grade;

	@NotNull(message = "최소값은 필수입니다.")
	private Double min;

	@NotNull(message = "최대값은 필수입니다.")
	private Double max;

	@NotNull(message = "점수는 필수입니다.")
	private Double score;

	@NotNull(message = "최소단위 포함여부는 필수입니다.")
	private Boolean minInclusive;

	@NotNull(message = "최대단위 포함여부는 필수입니다.")
	private Boolean maxInclusive;

	public static ServiceTargetDto from(ServiceTarget serviceTarget) {
		return new ServiceTargetDto(serviceTarget.getGrade(),
			serviceTarget.getMin(),
			serviceTarget.getMax(),
			serviceTarget.getScore(),
			serviceTarget.isMinInclusive(),
			serviceTarget.isMaxInclusive());
	}
}
