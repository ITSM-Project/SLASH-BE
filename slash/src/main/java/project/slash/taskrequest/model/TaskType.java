package project.slash.taskrequest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
	private String taskType;
	@Column(name = "task_detail")
	private String taskDetail;
	private int deadline;
	@Column(name = "service_relevance")
	private boolean serviceRelevance;

	private TaskType(String taskType, String taskDetail, int deadline, boolean serviceRelevance) {
		this.taskType = taskType;
		this.taskDetail = taskDetail;
		this.deadline = deadline;
		this.serviceRelevance = serviceRelevance;
	}

	public static TaskType from(CreateTaskTypeDto createTaskTypeDto) {
		return new TaskType(createTaskTypeDto.getTaskType(), createTaskTypeDto.getTaskDetail(),
			createTaskTypeDto.getDeadline(), createTaskTypeDto.isServiceRelevance());
	}
}
