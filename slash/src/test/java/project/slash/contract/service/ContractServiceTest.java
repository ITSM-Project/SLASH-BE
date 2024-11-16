package project.slash.contract.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.contract.repository.contract.ContractRepository;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class ContractServiceTest {

	@Autowired private ContractService contractService;
	@Autowired private ContractRepository contractRepository;
	@Autowired private TotalTargetRepository totalTargetRepository;

	@AfterEach
	void tearDown() {
		totalTargetRepository.deleteAllInBatch();
		contractRepository.deleteAllInBatch();
	}

	@DisplayName("계약을 생성하고 저장할 수 있다.")
	@Test
	void createContract(){
	    //given
		ContractRequestDto contractRequestDto = createContractRequestDto();

		//when
		Long contractId = contractService.createContract(contractRequestDto);

		//then
		Assertions.assertThat(contractId).isNotNull();
	}

	private static ContractRequestDto createContractRequestDto() {
		List<GradeDto> totalTargets =
			List.of(new GradeDto("A", 100.0, 100.0, 100, true, true));

		LocalDate startDate = LocalDate.of(2024, 10, 11);
		LocalDate endDate = LocalDate.of(2025, 10, 11);

		return new ContractRequestDto("테스트 계약서", startDate, endDate, totalTargets);
	}
}