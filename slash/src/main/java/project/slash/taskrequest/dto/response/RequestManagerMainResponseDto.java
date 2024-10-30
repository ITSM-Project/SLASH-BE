package project.slash.taskrequest.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestManagerMainResponseDto {
	List<StatusCountDto> statusCountList;
	List<TaskTypeCountDto> taskTypeCountList;
	List<SystemCountDto> systemCountList;
}
