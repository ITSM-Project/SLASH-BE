package project.slash.contract.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import project.slash.contract.dto.response.ContractDetailDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.repository.contract.ContractRepository;
import project.slash.contract.service.ContractService;
import project.slash.user.model.User;
import project.slash.user.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)  // Spring Security Filter 무시
class ContractControllerTest {

	@Autowired private MockMvc mockMvc;

	@MockBean private ContractService contractService;

	@Autowired private ObjectMapper objectMapper;

	@MockBean private ContractRepository contractRepository;

	@Autowired private UserRepository userRepository;

	@BeforeEach
	void initUser() {
		User user = User.from("contract", "CONTRACT_MANAGER", "test", "1234", "test@test.com", "010-1111-1111");
		userRepository.save(user);
	}

	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

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

	@DisplayName("모든 계약 정보를 조회할 수 있다.")
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
		return  new ContractDetailDto(
			1L,
			"테스트 계약",
			LocalDate.now(),
			LocalDate.now().plusYears(1),
			false,
			List.of(new GradeDto("A", 90.0, 100.0, 50, true, true)),
			List.of(new EvaluationItemDetailDto())
		);
	}
}
