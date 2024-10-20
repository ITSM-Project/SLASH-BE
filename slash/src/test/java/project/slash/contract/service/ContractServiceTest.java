package project.slash.contract.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.slash.contract.dto.request.CreateContractDto;

@SpringBootTest
@ActiveProfiles("test")
class ContractServiceTest {
	@Autowired
	private ContractService contractService;

	@DisplayName("계약 생성을 성공하면 계약 아이디를 반환한다.")
	@Test
	void createContract() {
		//given
		LocalDate startDate = LocalDate.parse("2023-10-11");
		LocalDate endDate = LocalDate.parse("2024-10-11");
		CreateContractDto createContractDto = new CreateContractDto("companyName", startDate, endDate);

		//when
		Long contractId = contractService.createContract(createContractDto);

		//then
		assertThat(contractId).isNotNull();
	}
}
