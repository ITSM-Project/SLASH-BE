package project.slash.taskrequest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {
	@NotNull(message = "계약 아이디는 필수입니다.")
	private Long contractId;

	@NotEmpty(message = "장비 이름은 필수입니다.")
	private String equipmentName;

	@NotEmpty(message = "업무 유형 상세는 필수입니다.")
	private String taskDetail;

	private boolean serviceRelevance;

	@NotEmpty(message = "제목은 필수입니다.")
	private String title;

	@NotEmpty(message = "내용은 필수입니다.")
	private String content;
}
