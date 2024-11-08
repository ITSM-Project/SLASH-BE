package project.slash.taskrequest.dto.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AllTaskTypeDto {
	private String type;
	private Set<String> typeDetails;
}
