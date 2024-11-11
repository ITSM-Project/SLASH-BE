package project.slash.statistics.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.slash.contract.model.Contract;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.statistics.dto.request.RequestStatisticsDto;
import project.slash.statistics.repository.StatisticsRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsSchedulingService {
	private static final String EVERY_LAST_DAY_OF_MONTH = "0 0 0 L * ?";	//매월의 마지막 달
	private final StatisticsService statisticsService;
	private final StatisticsRepository statisticsRepository;
	private final EvaluationItemRepository evaluationItemRepository;
	private final ContractRepository contractRepository;
	public void calculateStatistics() {
		LocalDate now = LocalDate.now();

		log.info("통계 스케줄러 실행 {}", now);

		List<Contract> allContract = contractRepository.findAll();

		for (Contract contract : allContract) {
			List<EvaluationItem> unCalculatedEvaluationItem = evaluationItemRepository.findUnCalculatedEvaluationItem(
				contract.getId(), now);

			for (EvaluationItem evaluationItem : unCalculatedEvaluationItem) {
				RequestStatisticsDto requestStatisticsDto = new RequestStatisticsDto(evaluationItem.getId(), now);

				statisticsService.createServiceTaskStatics(requestStatisticsDto);	//서비스 요청 적기처리율
			}
		}
	}
}
