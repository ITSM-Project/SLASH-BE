package project.slash.statistics.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestStatisticsDto {

	@NotNull(message = "조회할 서비스 아이디는 필수입니다.")
	private Long evaluationItemId;
	@NotNull(message = "통계 조회일은 필수입니다.")
	private LocalDate date;
}
