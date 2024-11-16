package project.slash.contract.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.AllContractDto;
import project.slash.contract.dto.response.ContractDetailDto;
import project.slash.contract.dto.response.ContractNameDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.repository.contract.ContractRepository;
import project.slash.contract.service.ContractService;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)  // Spring Security Filter 무시
class ContractControllerTest {

	@Autowired private MockMvc mockMvc;

	@MockBean private ContractService contractService;

	@Autowired private ObjectMapper objectMapper;

	@MockBean private ContractRepository contractRepository;


	@DisplayName("계약을 생성할 수 있다.")
	@Test
	void createContract() throws Exception {
	    //given
		ContractRequestDto contractRequestDto = createContractRequestDto();

		when(contractService.createContract(any())).thenReturn(1L);

		//when & then
		mockMvc.perform(post("/contract-manager/contract")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(contractRequestDto)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.data").value(1L));
	}

	@DisplayName("특정 계약의 모든 정보를 조회할 수 있다.")
	@Test
	void showAllContractInfo() throws Exception {
	    //given
		Long contractId = 1L;
		ContractDetailDto contractDetailDto = createContractDetailDto();

		when(contractService.showAllContractInfo(contractId)).thenReturn(contractDetailDto);

		//when & then
		mockMvc.perform(get("/common/contract/{contractId}", contractId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.contractId").value(1L))
			.andExpect(jsonPath("$.data.contractName").value("테스트 계약"))
			.andExpect(jsonPath("$.data.startDate").value(LocalDate.now().toString()))
			.andExpect(jsonPath("$.data.isTerminate").value(false))
			.andExpect(jsonPath("$.data.totalTargets[0].grade").value("A"));
	}

	@DisplayName("모든 계약을 조회할 수 있다.")
	@Test
	void showAllContract() throws Exception {
	    //given
		when(contractService.showAllContract()).thenReturn(List.of(createAllContractDto()));

	    //when & then
		mockMvc.perform(get("/contract-manager/all-contract")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].contractId").value(1L))
			.andExpect(jsonPath("$.data[0].contractName").value("테스트 계약"))
			.andExpect(jsonPath("$.data[0].terminate").value(false));
	}

	@DisplayName("계약을 삭제할 수 있다.")
	@Test
	void deleteContract() throws Exception {
	    //given
		Long contactId = 1L;

	    //when & then
		mockMvc.perform(delete("/contract-manager/contract/{contractId}", contactId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true));
	}

	@DisplayName("모든 계약 이름을 조회할 수 있다.")
	@Test
	void showAllContractName() throws Exception {
	    //given
		List<ContractNameDto> contractNameDtos = List.of(new ContractNameDto(1L, "테스트 계약1"),
			new ContractNameDto(2L, "테스트 계약2"));

		when(contractService.showAllContractName()).thenReturn(contractNameDtos);

		//when & then
		mockMvc.perform(get("/common/all-contract-name")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].contractId").value(1L))
			.andExpect(jsonPath("$.data[0].contractName").value("테스트 계약1"))
			.andExpect(jsonPath("$.data[1].contractId").value(2L))
			.andExpect(jsonPath("$.data[1].contractName").value("테스트 계약2"));
	}

	@DisplayName("종합 평가 등급을 수정할 수 있다.")
	@Test
	void updateTotalTarget() throws Exception {
		//given
		Long contractId = 1L;
		List<GradeDto> gradeDtos = createGradeDtos();

		//when & then
		mockMvc.perform(put("/contract-manager/total-target/{contractId}", contractId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(gradeDtos)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true));
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

	private ContractDetailDto createContractDetailDto() {
		return new ContractDetailDto(
			1L,
			"테스트 계약",
			LocalDate.now(),
			LocalDate.now().plusYears(1),
			false,
			List.of(new GradeDto("A", 90.0, 100.0, 50, true, true)),
			List.of(new EvaluationItemDetailDto())
		);
	}

	private AllContractDto createAllContractDto() {
		return new AllContractDto(1L,
			"테스트 계약",
			LocalDate.now(),
			LocalDate.now().plusYears(1),
			false);
	}
}
