package project.slash.taskrequest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import project.slash.system.model.Equipment;
import project.slash.taskrequest.model.constant.RequestStatus;
import project.slash.user.model.User;

@Entity
public class TaskRequest extends BaseTimeEntity{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private Long id;
	@Column(name = "additional_time")
	private int additionalTime;
	private String title;
	private String content;
	@Column(name = "due_on_time")
	private boolean dueOnTime;
	@Enumerated(EnumType.STRING)
	private RequestStatus status;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_type_id")
	private TaskType taskType;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_id")
	private User requester;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private User manager;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "equipment_id")
	private Equipment equipment;
}
