package project.slash.taskrequest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskTypeCountDto {
	String name;
	long count;
}
