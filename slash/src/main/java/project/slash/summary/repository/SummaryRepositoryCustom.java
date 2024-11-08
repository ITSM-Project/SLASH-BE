package project.slash.summary.repository;

import project.slash.summary.dto.evaluation.item.IncidentResolvedRateDto;
import project.slash.summary.dto.evaluation.item.ServiceResolvedRateDto;
import project.slash.summary.dto.evaluation.item.ServiceRuntimeRateDto;

public interface SummaryRepositoryCustom {

	public ServiceRuntimeRateDto getServiceRuntimeRate(Long evaluationItemId, String targetSystem,
		String targetEquipment, String lastDate);

	public IncidentResolvedRateDto getIncidentResolvedRate(Long evaluationItemId, String targetSystem,
		String targetEquipment, String lastDate);

	public ServiceResolvedRateDto getServiceResolvedRate(Long evaluationItemId, String targetSystem,
		String targetEquipment, String lastDate);
}
