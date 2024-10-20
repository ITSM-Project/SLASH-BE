package project.slash.taskrequest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TaskType {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_type_id")
	private Long id;
	@Column(name = "task_type")
	private String taskType;
	@Column(name = "task_detail")
	private String taskDetail;
	private int deadline;
	private int priority;
	@Column(name = "service_relevance")
	private boolean serviceRelevance;
}
