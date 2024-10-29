package project.slash.taskrequest.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TaskTypeDto {
	private String type;
	private List<String> typeDetails;
}
