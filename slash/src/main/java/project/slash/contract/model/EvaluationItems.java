package project.slash.contract.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.request.EvaluationItemDto;
import project.slash.taskrequest.model.TaskType;

@Entity
@Table(name = "evaluation_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EvaluationItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evalutation_item_id")
	private Long id;

	private String category;	//서비스 항목

	private int weight;

	private String period;

	private String purpose;

	private String formula;

	private String unit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id")
	private Contract contract;

	@OneToMany(mappedBy = "evaluationItem", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ServiceTarget> serviceTargets = new ArrayList<>();

	@OneToMany(mappedBy = "evaluationItem", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskType> taskTypes = new ArrayList<>();

	public static EvaluationItems of(EvaluationItemDto itemDto, Contract contract) {
		EvaluationItems evaluationItem = EvaluationItems.builder()
			.category(itemDto.getCategory())
			.weight(itemDto.getWeight())
			.period(itemDto.getPeriod())
			.purpose(itemDto.getPurpose())
			.formula(itemDto.getFormula())
			.unit(itemDto.getUnit())
			.contract(contract)
			.serviceTargets(itemDto.getServiceTargets().stream().map(ServiceTarget::from).toList())
			.taskTypes(itemDto.getTaskTypes().stream().map(TaskType::from).toList())
			.build();

		evaluationItem.addEvaluationItemsToTaskTypes();
		evaluationItem.addServiceTargets();

		return evaluationItem;
	}

	private void addServiceTargets() {
		serviceTargets.forEach(target -> target.setEvaluationItem(this));
	}

	private void addEvaluationItemsToTaskTypes() {
		this.taskTypes.forEach(taskType -> taskType.setEvaluationItems(this));
	}
}
