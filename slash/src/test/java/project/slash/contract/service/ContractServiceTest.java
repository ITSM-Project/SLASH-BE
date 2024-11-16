package project.slash.contract.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static project.slash.contract.exception.ContractErrorCode.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.AllContractDto;
import project.slash.contract.dto.response.ContractNameDto;
import project.slash.contract.mapper.ContractMapper;
import project.slash.contract.mapper.TotalTargetMapper;
import project.slash.contract.model.Contract;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.contract.repository.contract.ContractRepository;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {
	@Mock private ContractRepository contractRepository;
	@Mock private TotalTargetRepository totalTargetRepository;
	@Mock private TotalTargetMapper totalTargetMapper;
	@Mock private ContractMapper contractMapper;

	@InjectMocks
	private ContractService contractService;

	@DisplayName("계약을 생성하고 저장할 수 있다.")
	@Test
	void createContract(){
		// given
		Contract savedContract = createTestContract(1L, "테스트 계약");
		ContractRequestDto contractRequestDto = createContractRequestDto();

		given(contractRepository.save(any())).willReturn(savedContract);

		// when
		Long contractId = contractService.createContract(contractRequestDto);

		// then
		assertThat(contractId).isNotNull();
	}

	@DisplayName("종합 평가 등급을 업데이트 할 수 있다.")
	@Test
	void updateTotalTarget(){
		// given
		Long contractId = 1L;
		Contract contract = createTestContract(contractId, "테스트 계약");
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
		// given
		Long contractId = 1L;
		Contract contract = createTestContract(contractId, "테스트 계약");
		List<TotalTarget> oldTotalTargets = createTotalTargets(contract);
		List<GradeDto> newGradeDtos = createGradeDtos();

		when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
		when(totalTargetRepository.findByContractIdAndIsActiveTrue(contractId)).thenReturn(oldTotalTargets);

		// when
		contractService.newTotalTarget(contractId, newGradeDtos);

		// then
		verify(totalTargetRepository).saveAll(any());
	}

	@DisplayName("계약을 삭제하면 종료 여부가 true로 변경된다.")
	@Test
	void deleteContract() {
		// given
		Long contractId = 1L;
		Contract contract = spy(Contract.builder()
			.id(contractId)
			.endDate(LocalDate.now().minusDays(1))  // 어제 날짜로 설정
			.build());

		when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

		// when
		contractService.deleteContract(contractId);

		// then
		verify(contract, times(1)).terminate();
	}

	@DisplayName("종료되지 않은 계약을 삭제하면 예외가 발생한다.")
	@Test
	void deleteContractBeforeTerminate(){
		// given
		Long contractId = 1L;
		Contract contract = createTestContract(contractId, "테스트 계약");
		when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

		// when & then
		assertThatThrownBy(() -> contractService.deleteContract(contractId))
			.isInstanceOf(BusinessException.class)
			.hasFieldOrPropertyWithValue("errorCode", NOT_TERMINATE_CONTRACT);
	}

	@DisplayName("모든 계약 정보를 조회할 수 있다.")
	@Test
	void showAllContract(){
		// given
		List<Contract> contracts = List.of(createTestContract(1L, "테스트 계약1"),
			createTestContract(2L, "테스트 계약 2"));

		List<AllContractDto> allContractDtos = List.of(
			new AllContractDto(1L, "테스트 계약1", LocalDate.now(), LocalDate.now().plusMonths(1), false),
			new AllContractDto(2L, "테스트 계약2", LocalDate.now(), LocalDate.now().plusMonths(1), false));

		when(contractRepository.findAllByOrderByStartDateDesc()).thenReturn(contracts);
		when(contractMapper.toAllContractDtoList(contracts)).thenReturn(allContractDtos);

		// when
		List<AllContractDto> result = contractService.showAllContract();

		// then
		assertThat(result).hasSize(2)
			.extracting("contractId", "contractName")
			.containsExactly(Tuple.tuple(1L, "테스트 계약1"),
				Tuple.tuple(2L, "테스트 계약2"));
	}

	@DisplayName("모든 계약의 협약서 이름을 조회할 수 있다.")
	@Test
	void showAllContractName() {
		//given
		List<Contract> contracts = List.of(createTestContract(1L, "테스트 계약1"),
			createTestContract(2L, "테스트 계약2"));

		List<ContractNameDto> contractNameDtos = List.of(
			new ContractNameDto(1L, "테스트 계약1"),
			new ContractNameDto(2L, "테스트 계약2")
		);

		when(contractRepository.findAll()).thenReturn(contracts);
		when(contractMapper.toAllContractNameList(contracts)).thenReturn(contractNameDtos);

		//when
		List<ContractNameDto> result = contractService.showAllContractName();

		//then
		assertThat(result).hasSize(2)
			.extracting("contractId", "contractName")
			.containsExactly(Tuple.tuple(1L, "테스트 계약1"),
				Tuple.tuple(2L, "테스트 계약2"));
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

	private Contract createTestContract(Long contractId, String contractName) {
		return Contract.builder()
			.id(contractId)
			.contractName(contractName)
			.startDate(LocalDate.now())
			.endDate(LocalDate.now().plusMonths(1))
			.isTerminate(false)
			.build();
	}
}
