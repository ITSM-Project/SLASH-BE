package project.slash.contract.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import project.slash.contract.dto.request.CreateDetailDto;

@Entity
@Table(name = "service_detail")
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_detail_id")
	private Long id;

	private int weight;

	private String period;

	private String purpose;

	private String formula;

	private String unit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_item_id")
	private EvaluationItem evaluationItem;

	public static ServiceDetail from(CreateDetailDto detailDto, EvaluationItem evaluationItem) {
		return ServiceDetail.builder()
			.weight(detailDto.getWeight())
			.period(detailDto.getPeriod())
			.purpose(detailDto.getPurpose())
			.formula(detailDto.getFormula())
			.unit(detailDto.getUnit())
			.evaluationItem(evaluationItem)
			.build();
	}
}
