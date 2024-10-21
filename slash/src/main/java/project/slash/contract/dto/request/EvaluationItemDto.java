package project.slash.contract.dto.request;

import java.util.List;

import lombok.Getter;
import project.slash.contract.model.ServiceTarget;

@Getter
public class EvaluationItemDto {
	private String category;
	private int weight;
	private String period;

	private List<ServiceTarget> serviceTargets;
}
