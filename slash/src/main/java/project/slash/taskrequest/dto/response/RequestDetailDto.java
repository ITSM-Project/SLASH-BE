package project.slash.taskrequest.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.taskrequest.model.TaskRequest;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class RequestDetailDto {
	private Long requestId;
	private String taskType;
	private String status;
	private boolean dueOnTime;
	private String system;
	private String equipmentName;
	private String taskDetail;
	private String title;
	private String content;
	private String requester;
	private String manager;
	private String managerId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime requestTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;
}
