package project.slash.taskrequest.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.taskrequest.model.constant.RequestStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestManagementDto {
	private String title;
	private String content;
	private Boolean dueOnTime;
	private RequestStatus status;
	private String equipmentName;
	private String type;
	private String taskDetail;
	private Long id;
	private Long contractId;
	private String requesterName;
	private String managerName;
	private String managerId;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;

}

