package project.slash.contract.repository;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
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
		Assertions.assertThat(result).hasSize(2)
			.extracting("startDate", "endDate")
			.containsExactlyInAnyOrder(Tuple.tuple(LocalDate.of(2024, 10, 12), LocalDate.of(2025, 10, 11)),
				Tuple.tuple(LocalDate.of(2023, 10, 11), LocalDate.of(2024, 10, 11)));
	}

	private static Contract createContract(LocalDate startDate, LocalDate endDate, boolean isTerminate) {
		return Contract.builder()
			.companyName("테스트 회사")
			.startDate(startDate)
			.endDate(endDate)
			.isTerminate(isTerminate)
			.build();
	}
}
