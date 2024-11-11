package project.slash.contract.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.slash.contract.model.Contract;
import project.slash.contract.repository.ContractRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractSchedulingService {
	private static final String EVERY_MIDNIGHT = "0 0 0 * * *";
	private final ContractRepository contractRepository;

	@Scheduled(cron = EVERY_MIDNIGHT)
	public void terminateContract() {
		List<Contract> contractsEndingToday = contractRepository.findByEndDate(LocalDate.now());
		contractsEndingToday.forEach(Contract::terminate);
	}
}
