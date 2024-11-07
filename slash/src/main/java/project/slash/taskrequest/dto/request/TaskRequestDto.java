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
	@NotEmpty(message = "장비 이름은 필수입니다.")
	private String equipmentName;

	@NotEmpty(message = "요청 유형 아이디는 필수입니다.")
	private Long taskTypeId;

	@NotEmpty(message = "제목은 필수입니다.")
	private String title;

	@NotEmpty(message = "내용은 필수입니다.")
	private String content;
}
