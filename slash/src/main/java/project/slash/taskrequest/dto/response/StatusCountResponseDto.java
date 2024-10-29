package project.slash.taskrequest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusCountResponseDto {
	private long total;
	private long registered;
	private long processing;
	private long completed;
}
