package project.slash.contract.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static project.slash.contract.exception.EvaluationItemErrorCode.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.TaskTypeDto;
import project.slash.contract.dto.request.CreateEvaluationItemDto;
import project.slash.contract.mapper.EvaluationItemMapper;
import project.slash.contract.mapper.ServiceTargetMapper;
import project.slash.contract.model.Contract;
import project.slash.contract.model.EvaluationItem;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.ServiceTargetRepository;
import project.slash.contract.repository.contract.ContractRepository;
import project.slash.contract.repository.evaluationItem.EvaluationItemRepository;
import project.slash.taskrequest.mapper.TaskTypeMapper;
import project.slash.taskrequest.repository.TaskTypeRepository;

@ExtendWith(MockitoExtension.class)
class EvaluationItemServiceTest {
	@Mock private ContractRepository contractRepository;
	@Mock private EvaluationItemRepository evaluationItemRepository;
	@Mock private ServiceTargetRepository serviceTargetRepository;
	@Mock private TaskTypeRepository taskTypeRepository;
	@Mock private EvaluationItemMapper evaluationItemMapper;
	@Mock private ServiceTargetMapper serviceTargetMapper;
	@Mock private TaskTypeMapper taskTypeMapper;

	@InjectMocks private EvaluationItemService evaluationItemService;


	@DisplayName("서비스 평가 항목을 생성할 수 있다.")
	@Test
	void createEvaluationItem(){
		// given
		Long contractId = 1L;
		CreateEvaluationItemDto createEvaluationItemDto = createEvaluationItemDto(contractId);
		Contract contract = createTestContract(contractId, "테스트 계약서");
		EvaluationItem evaluationItem = evaluationItemMapper.toEntity(createEvaluationItemDto , contract);

		when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
		when(evaluationItemMapper.toEntity(createEvaluationItemDto , contract)).thenReturn(evaluationItem);

		// when
		evaluationItemService.createEvaluationItem(createEvaluationItemDto );

		// then
		verify(evaluationItemRepository).save(evaluationItem);
	}

	@DisplayName("새로운 서비스 평가 항목을 생성하면 기존 서비스 평가항목은 비활성화 된다.")
	@Test
	void newEvaluationItem(){
		// given
		Long evaluationItemId = 1L;
		Long contractId = 1L;

		CreateEvaluationItemDto evaluationItemDto = createEvaluationItemDto(contractId);
		Contract contract = createTestContract(contractId, "테스트 계약서");
		EvaluationItem oldItem = createEvaluationItem(contract);
		EvaluationItem newItem = createEvaluationItem(contract);

		when(evaluationItemRepository.findById(evaluationItemId)).thenReturn(Optional.of(oldItem));
		when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
		when(evaluationItemMapper.toEntity(evaluationItemDto, contract)).thenReturn(newItem);

		// when
		evaluationItemService.newEvaluationItem(evaluationItemId, evaluationItemDto);

		// then
		assertThat(oldItem.isActive()).isFalse();
		verify(evaluationItemRepository).save(any(EvaluationItem.class));
	}

	@DisplayName("존재하지 않는 서비스 평가 항목 아이디으로 새 서비스 평가 항목을 생성하면 예외가 발생한다.")
	@Test
	void newEvaluationItemWithNotFoundEvaluationItemId(){
	    //given
		Long unknownEvaluationItemId = 99L;
		Long contractId = 1L;
		CreateEvaluationItemDto evaluationItemDto = createEvaluationItemDto(contractId);

		//when & then
		assertThatThrownBy(() -> evaluationItemService.updateEvaluationItem(unknownEvaluationItemId, evaluationItemDto))
			.isInstanceOf(BusinessException.class)
			.hasFieldOrPropertyWithValue("errorCode", NOT_FOUND_ITEMS);
	}

	private static EvaluationItem createEvaluationItem(Contract contract) {
		return EvaluationItem.builder()
			.category("가용성")
			.weight(30)
			.period("월간")
			.purpose("서비스 가용성 측정")
			.formula("(정상 서비스 시간 / 전체 서비스 시간) * 100")
			.unit("%")
			.isActive(true)
			.contract(contract)
			.build();
	}

	private CreateEvaluationItemDto createEvaluationItemDto(Long contractId) {
		return CreateEvaluationItemDto.builder()
			.contractId(contractId)
			.category("서비스 가동률")
			.weight(40)
			.period("월별")
			.purpose("목적")
			.formula("산출식")
			.unit("단위")
			.taskTypes(createTaskTypeDtos())
			.serviceTargets(createGradeDtos())
			.build();
	}

	private List<GradeDto> createGradeDtos() {
		return List.of(new GradeDto("A", 100.0, 100.0, 100, true, true));
	}

	private List<TaskTypeDto> createTaskTypeDtos() {
		return List.of(new TaskTypeDto("서비스 요청", "업무 지원", 0, false, false));
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
