package project.slash.contract.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.request.EvaluationItemDto;
import project.slash.contract.constant.Category;

@Entity
@Table(name = "evaluation_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvaluationItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evalutation_item_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private Category category;

	private int weight;

	private String period;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id")
	private Contract contract;

	@OneToMany(mappedBy = "evaluationItems", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ServiceTarget> serviceTargets = new ArrayList<>();

	private EvaluationItems(Category category, int weight, String period, Contract contract) {
		this.category = category;
		this.weight = weight;
		this.period = period;
		this.contract = contract;
	}

	public static EvaluationItems of(EvaluationItemDto evaluationItemDto, Contract contract) {
		Category category = Category.getCategory(evaluationItemDto.getCategory());

		return new EvaluationItems(category, evaluationItemDto.getWeight(), evaluationItemDto.getPeriod(), contract);
	}

	public void addServiceTargets(List<ServiceTarget> serviceTargets) {
		for (ServiceTarget serviceTarget : serviceTargets) {
			serviceTarget.setEvaluationItems(this);
			this.serviceTargets.add(serviceTarget);
		}
	}
}
