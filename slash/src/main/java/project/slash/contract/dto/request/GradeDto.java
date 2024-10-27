package project.slash.contract.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GradeDto {
	@NotBlank(message = "등급은 필수입니다.")
	private String grade;

	@NotNull(message = "최소값은 필수입니다.")
	private double min;

	@NotNull(message = "최대값은 필수입니다.")
	private double max;

	@NotNull(message = "점수는 필수입니다.")
	private double score;

	@NotNull(message = "최소단위 포함여부는 필수입니다.")
	private boolean minInclusive;

	@NotNull(message = "최대단위 포함여부는 필수입니다.")
	private boolean maxInclusive;
}
