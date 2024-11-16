package project.slash.contract.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.ContractDetailDto;
import project.slash.contract.mapper.ContractMapper;
import project.slash.contract.mapper.TotalTargetMapper;
import project.slash.contract.model.Contract;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.contract.repository.contract.ContractRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

	@Mock private TotalTargetRepository totalTargetRepository;
	@Mock private ContractRepository contractRepository;
	@Mock private EvaluationItemRepository evaluationItemRepository;
	@Mock private ContractMapper contractMapper;
	@Mock private TotalTargetMapper totalTargetMapper;

	@InjectMocks
	private ContractService contractService;


	@DisplayName("계약을 생성하고 저장할 수 있다.")
	@Test
	void createContract(){
		// given
		ContractRequestDto contractRequestDto = createContractRequestDto();
		Contract savedContract = createTestContract(1L);

		given(contractRepository.save(any())).willReturn(savedContract);

		// when
		Long result = contractService.createContract(contractRequestDto);

		// then
		assertThat(result).isEqualTo(1L);
		then(contractRepository).should().save(any());
	}

	@DisplayName("특정 계약의 모든 정보를 확인할 수 있다.")
	@Test
	void showAllContractInfo(){
		// given
		Long contractId = 1L;
		Contract contract = createTestContract(contractId);
		ContractDetailDto contractDetailDto = new ContractDetailDto();

		given(contractRepository.findById(contractId)).willReturn(Optional.of(contract));
		given(evaluationItemRepository.findAllEvaluationItems(contractId)).willReturn(Collections.emptyList());
		given(contractMapper.toContractDetailDto(eq(contract), any(), any())).willReturn(contractDetailDto);

		// when
		ContractDetailDto result = contractService.showAllContractInfo(contractId);

		// then
		assertThat(result).isEqualTo(contractDetailDto);
		then(contractRepository).should().findById(contractId);
		then(evaluationItemRepository).should().findAllEvaluationItems(contractId);
	}

	private Contract createTestContract(Long contractId) {
		return Contract.builder()
			.id(contractId)
			.startDate(LocalDate.of(2024, 10, 11))
			.endDate(LocalDate.of(2025, 10, 11))
			.isTerminate(false)
			.contractName("테스트 계약").build();
	}

	private ContractRequestDto createContractRequestDto() {
		List<GradeDto> totalTargets = createTotalTargets();

		LocalDate startDate = LocalDate.of(2024, 10, 11);
		LocalDate endDate = LocalDate.of(2025, 10, 11);

		return new ContractRequestDto("테스트 계약서", startDate, endDate, totalTargets);
	}

	private List<GradeDto> createTotalTargets() {
		return List.of(new GradeDto("A", 100.0, 100.0, 100, true, true));
	}
}
