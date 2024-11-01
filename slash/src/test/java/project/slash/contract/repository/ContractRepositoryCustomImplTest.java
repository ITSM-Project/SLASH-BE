package project.slash.contract.repository;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.slash.contract.dto.ContractDataDto;

@SpringBootTest
@ActiveProfiles("test")
class ContractRepositoryCustomImplTest {
	@Autowired private ContractRepository ContractRepository;

	@Test
	@DisplayName("자동계산 지표")
	void findIndicatorByCategory() {
		List<ContractDataDto> contractDataDtoList = ContractRepository.findIndicatorByCategory("서비스 가동률");

		// 순수한 값만 출력
		contractDataDtoList.forEach(System.out::println);
		System.out.println("Total items: " + contractDataDtoList.size());

	}
}
