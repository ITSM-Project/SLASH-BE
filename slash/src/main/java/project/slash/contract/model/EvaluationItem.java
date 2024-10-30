package project.slash.contract.model;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evaluation_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvaluationItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluation_item_id")
	private Long id;

	private String category;    //서비스 항목

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id")
	private Contract contract;

	private EvaluationItem(String category) {
		this.category = category;
	}

	public static EvaluationItem from(String category) {
		return new EvaluationItem(category);
	}

	void setContract(Contract contract) {
		this.contract = contract;
		if (!contract.getEvaluationItems().contains(this)) {
			contract.getEvaluationItems().add(this);
		}
	}
}
