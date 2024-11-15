package project.slash.statistics.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.slash.contract.model.Contract;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.repository.contract.ContractRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.statistics.dto.request.RequestStatisticsDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsSchedulingService {
	private static final String EVERY_LAST_DAY_OF_MONTH = "0 0 0 L * ?";    //매월의 마지막 달
	private final AutoStatisticsService autoStatisticsService;
	private final EvaluationItemRepository evaluationItemRepository;
	private final ContractRepository contractRepository;
	private Map<String, Consumer<RequestStatisticsDto>> statisticsActions;

	@PostConstruct
	public void init() {
		statisticsActions = Map.of(
			"서비스 가동률", autoStatisticsService::createMonthlyStats,
			"장애 적기처리율", autoStatisticsService::addIncidentStatistics,
			"서비스요청 적기처리율", autoStatisticsService::createServiceTaskStatistics
		);
	}

	@Scheduled(cron = EVERY_LAST_DAY_OF_MONTH)
	public void calculateStatistics() {
		LocalDate now = LocalDate.now();
		log.info("통계 스케줄러 실행 {}", now);

		List<Contract> activeContracts = contractRepository.findByIsTerminateFalse();

		for (Contract activeContract : activeContracts) {
			List<EvaluationItem> unCalculatedEvaluationItem = evaluationItemRepository.findUnCalculatedEvaluationItem(
				activeContract.getId(), now);
			for (EvaluationItem evaluationItem : unCalculatedEvaluationItem) {
				calculateStatisticsForItem(evaluationItem, now);
			}
		}
	}

	private void calculateStatisticsForItem(EvaluationItem evaluationItem, LocalDate now) {
		RequestStatisticsDto requestStatisticsDto = new RequestStatisticsDto(evaluationItem.getId(), YearMonth.now());
		Consumer<RequestStatisticsDto> action = statisticsActions.get(evaluationItem.getCategory());

		action.accept(requestStatisticsDto);
	}
}
