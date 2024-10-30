package project.slash.evaluationitem.model;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.evaluationitem.dto.request.CreateEvaluationItemDto;
import project.slash.contract.model.Contract;

@Entity
@Table(name = "evaluation_item")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvaluationItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluation_item_id")
	private Long id;

	private String category;    //서비스 항목

	private int weight;

	private String period;

	private String purpose;

	private String formula;

	private String unit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id")
	private Contract contract;

	public static EvaluationItem from(CreateEvaluationItemDto createEvaluationItemDto) {
		return EvaluationItem.builder()
			.category(createEvaluationItemDto.getCategory())
			.weight(createEvaluationItemDto.getWeight())
			.period(createEvaluationItemDto.getPeriod())
			.purpose(createEvaluationItemDto.getPurpose())
			.formula(createEvaluationItemDto.getFormula())
			.unit(createEvaluationItemDto.getUnit())
			.build();
	}
}
