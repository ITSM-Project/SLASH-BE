package project.slash.contract.model;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import project.slash.contract.dto.request.CreateEvaluationItemDto;

@Entity
@Table(name = "evaluation_item")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

	@Column(name = "is_active")
	private boolean isActive;

	@CreatedDate
	@Column(updatable = false)
	private LocalDate createDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id")
	private Contract contract;

	public static EvaluationItem from(CreateEvaluationItemDto createEvaluationItemDto, Contract contract) {
		return EvaluationItem.builder()
			.contract(contract)
			.category(createEvaluationItemDto.getCategory())
			.weight(createEvaluationItemDto.getWeight())
			.period(createEvaluationItemDto.getPeriod())
			.purpose(createEvaluationItemDto.getPurpose())
			.formula(createEvaluationItemDto.getFormula())
			.unit(createEvaluationItemDto.getUnit())
			.isActive(true)
			.build();
	}

	public void update(CreateEvaluationItemDto newEvaluationItem) {
		this.category = newEvaluationItem.getCategory();
		this.weight = newEvaluationItem.getWeight();
		this.period = newEvaluationItem.getPurpose();
		this.formula = newEvaluationItem.getFormula();
		this.unit = newEvaluationItem.getUnit();
	}

	public void deactivate() {
		this.isActive = false;
	}
}
