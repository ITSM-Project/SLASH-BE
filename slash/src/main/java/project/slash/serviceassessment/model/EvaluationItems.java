package project.slash.serviceassessment.model;

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
import project.slash.contract.model.Contract;
import project.slash.serviceassessment.model.constant.Category;

@Entity
public class EvaluationItems {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evalutation_item_id")
	private Long id;
	@Enumerated(EnumType.STRING)
	private Category category;
	private int weight;
	private String period;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id")
	private Contract contract;
}
