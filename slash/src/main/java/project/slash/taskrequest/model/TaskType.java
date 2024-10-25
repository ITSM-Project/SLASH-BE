package project.slash.taskrequest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import project.slash.contract.model.EvaluationItems;
import project.slash.taskrequest.dto.request.CreateTaskTypeDto;

@Entity
@Table(name = "task_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_type_id")
	private Long id;
	@Column(name = "task_type")
	private String taskType;	//장애 요청, 서비스 요청 ..
	@Column(name = "task_detail")
	private String taskDetail;	//단순 장애, 복합 장애 ..
	private int deadline;
	@Column(name = "service_relevance")
	private boolean serviceRelevance;

	@Column(name = "inclusion_status")
	private boolean inclusionStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_item_id")
	private EvaluationItems evaluationItems;

	private TaskType(String taskType, String taskDetail, int deadline, boolean serviceRelevance, boolean inclusionStatus) {
		this.taskType = taskType;
		this.taskDetail = taskDetail;
		this.deadline = deadline;
		this.serviceRelevance = serviceRelevance;
		this.inclusionStatus = inclusionStatus;
	}

	public static TaskType from(CreateTaskTypeDto createTaskTypeDto) {
		return new TaskType(createTaskTypeDto.getTaskType(), createTaskTypeDto.getTaskDetail(),
			createTaskTypeDto.getDeadline(), createTaskTypeDto.isServiceRelevance(), createTaskTypeDto.isInclusionStatus());
	}
}
