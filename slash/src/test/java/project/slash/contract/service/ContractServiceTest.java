package project.slash.contract.service;

import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.model.Contract;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.contract.repository.contract.ContractRepository;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class ContractServiceTest {

	@Autowired private ContractService contractService;
	@MockBean private ContractRepository contractRepository;
	@MockBean private TotalTargetRepository totalTargetRepository;

	@AfterEach
	void tearDown() {
		totalTargetRepository.deleteAllInBatch();
		contractRepository.deleteAllInBatch();
	}

	@DisplayName("계약을 생성하고 저장할 수 있다.")
	@Test
	void createContract(){
		//given
		Contract savedContract = createTestContract();
		ContractRequestDto contractRequestDto = createContractRequestDto();

		given(contractRepository.save(any())).willReturn(savedContract);

		//when
		Long contractId = contractService.createContract(contractRequestDto);

		//then
		Assertions.assertThat(contractId).isNotNull();
	}

	@DisplayName("종합 평가 등급을 업데이트 할 수 있다.")
	@Test
	void updateTotalTarget(){
		// given
		Long contractId = 1L;
		Contract contract = createTestContract();

		List<TotalTarget> oldTotalTargets = createTotalTargets(contract);
		List<GradeDto> newGradeDtos = createGradeDtos();

		when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
		when(totalTargetRepository.findByContractIdAndIsActiveTrue(contractId)).thenReturn(oldTotalTargets);

		// when
		contractService.updateTotalTarget(contractId, newGradeDtos);

		// then
		verify(totalTargetRepository).deleteAll(oldTotalTargets);
		verify(totalTargetRepository).saveAll(any());
	}

	@DisplayName("새로운 종합 평가 등급을 생성할 수 있다.")
	@Test
	void newTotalTarget(){
	    //given
		Long contractId = 1L;
		Contract contract = createTestContract();

		List<TotalTarget> oldTotalTargets = createTotalTargets(contract);
		List<GradeDto> newGradeDtos = createGradeDtos();

		when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
		when(totalTargetRepository.findByContractIdAndIsActiveTrue(contractId)).thenReturn(oldTotalTargets);

	    //when
		contractService.newTotalTarget(contractId, newGradeDtos);

	    //then
		verify(totalTargetRepository).saveAll(any());
	}

	private ContractRequestDto createContractRequestDto() {
		List<GradeDto> totalTargets = createGradeDtos();

		LocalDate startDate = LocalDate.of(2024, 10, 11);
		LocalDate endDate = LocalDate.of(2025, 10, 11);

		return new ContractRequestDto("테스트 계약서", startDate, endDate, totalTargets);
	}

	private List<GradeDto> createGradeDtos() {
		return List.of(new GradeDto("A", 100.0, 100.0, 100, true, true));
	}

	private List<TotalTarget> createTotalTargets(Contract contract) {
		return List.of(TotalTarget.builder()
				.grade("A")
				.min(100.0)
				.minInclusive(true)
				.max(100.0)
				.maxInclusive(true)
				.isActive(true)
				.contract(contract)
				.build());
	}

	private Contract createTestContract() {
		return Contract.builder()
			.id(1L)
			.contractName("테스트 계약")
			.startDate(LocalDate.now())
			.endDate(LocalDate.now().plusMonths(1))
			.isTerminate(false)
			.build();
	}
}
