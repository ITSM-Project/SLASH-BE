package project.slash.contract.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.slash.contract.model.Contract;

@SpringBootTest
@ActiveProfiles("test")
class ContractRepositoryTest {
	@Autowired private ContractRepository contractRepository;

	@AfterEach
	void tearDown() {
		contractRepository.deleteAllInBatch();
	}

	@DisplayName("모든 계약 정보를 시작 날짜를 기준으로 정렬해서 조회할 수 있다.")
	@Test
	void findAllByOrderByStartDateDesc(){
	    //given
		Contract contract1 = createContract(LocalDate.of(2023, 10, 11), LocalDate.of(2024, 10, 11), true);
		Contract contract2 = createContract(LocalDate.of(2024, 10, 12), LocalDate.of(2025, 10, 11), false);

		contractRepository.saveAll(List.of(contract1, contract2));

		//when
		List<Contract> result = contractRepository.findAllByOrderByStartDateDesc();

		//then
		assertThat(result).hasSize(2)
			.extracting("startDate", "endDate")
			.containsExactlyInAnyOrder(Tuple.tuple(LocalDate.of(2024, 10, 12), LocalDate.of(2025, 10, 11)),
				Tuple.tuple(LocalDate.of(2023, 10, 11), LocalDate.of(2024, 10, 11)));
	}

	@DisplayName("만료되지 않은 계약을 찾을 수 있다.")
	@Test
	void findByTerminateIsFalse(){
	    //given
		Contract contract = createContract(LocalDate.of(2024, 10, 12), LocalDate.of(2025, 10, 11), false);
	    contractRepository.save(contract);

		//when
		Contract result = contractRepository.findByIsTerminateFalse().get();

		//then
		assertThat(result).extracting("contractName", "startDate", "endDate")
			.containsExactly("테스트 회사",
				LocalDate.of(2024, 10, 12),
				LocalDate.of(2025, 10, 11));
	}

	private static Contract createContract(LocalDate startDate, LocalDate endDate, boolean isTerminate) {
		return Contract.builder()
			.contractName("테스트 회사")
			.startDate(startDate)
			.endDate(endDate)
			.isTerminate(isTerminate)
			.build();
	}
}
