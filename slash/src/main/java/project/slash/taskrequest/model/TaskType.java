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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.request.DetailDto;
import project.slash.contract.model.EvaluationItem;

@Entity
@Table(name = "task_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TaskType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_type_id")
	private Long id;

	private String type;	//장애 요청, 서비스 요청 ..

	@Column(name = "task_detail")
	private String taskDetail;	//단순 장애, 복합 장애 ..

	private int deadline;

	@Column(name = "service_relevance")
	private boolean serviceRelevance;

	@Column(name = "inclusion_status")
	private boolean inclusionStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_item_id")
	private EvaluationItem evaluationItem;

	public static TaskType from(DetailDto.TaskTypeDto taskTypeDto, EvaluationItem evaluationItem) {
		return TaskType.builder()
			.type(taskTypeDto.getType())
			.taskDetail(taskTypeDto.getTaskDetail())
			.deadline(taskTypeDto.getDeadline())
			.serviceRelevance(taskTypeDto.isServiceRelevance())
			.inclusionStatus(taskTypeDto.isInclusionStatus())
			.evaluationItem(evaluationItem)
			.build();
	}
}
