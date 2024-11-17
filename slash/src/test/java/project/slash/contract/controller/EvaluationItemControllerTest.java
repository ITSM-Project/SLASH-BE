package project.slash.contract.controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import project.slash.contract.dto.TaskTypeDto;
import project.slash.contract.dto.request.CreateEvaluationItemDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.service.EvaluationItemService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class EvaluationItemControllerTest {
	@Autowired private MockMvc mockMvc;

	@MockBean private EvaluationItemService evaluationItemService;

	@Autowired private ObjectMapper objectMapper;


	@DisplayName("새로운 서비스 평가 항목을 생성할 수 있다.")
	@Test
	void createEvaluationItem() throws Exception {
	    //given
		CreateEvaluationItemDto createEvaluationItemDto = createEvaluationItemDto(1L);

		//when & then
		mockMvc.perform(post("/contract-manager/evaluation-item")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(createEvaluationItemDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true));
	}

	@DisplayName("서비스 평가 항목에 대한 내용을 자세히 조회할 수 있다.")
	@Test
	void showCategoryDetail() throws Exception {
	    //given
		Long evaluationItemId = 1L;
		EvaluationItemDetailDto evaluationItemDetailDto = createEvaluationItemDetailDto(evaluationItemId);

		when(evaluationItemService.findDetailByItemId(evaluationItemId)).thenReturn(evaluationItemDetailDto);

		//when & then
		mockMvc.perform(get("/common/detail/{id}", evaluationItemId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.category").value("서비스 가동률"))
			.andExpect(jsonPath("$.data.weight").value(40))
			.andExpect(jsonPath("$.data.period").value("월별"))
			.andExpect(jsonPath("$.data.purpose").value("목적"))
			.andExpect(jsonPath("$.data.formula").value("산출식"));
	}

	@DisplayName("수정 가능 여부를 반환할 수 있다.")
	@Test
	void checkModifiable() throws Exception {
	    //given
		Long contractId = 1L;

		when(evaluationItemService.checkModifiable(contractId)).thenReturn(true);
	    //when & then
		mockMvc.perform(get("/contract-manager/modifiable/{contractId}", contractId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(true));
	}

	private EvaluationItemDetailDto createEvaluationItemDetailDto(Long evaluationItemId) {
		return EvaluationItemDetailDto.builder()
			.evaluationItemId(evaluationItemId)
			.category("서비스 가동률")
			.weight(40)
			.period("월별")
			.purpose("목적")
			.isAuto(true)
			.formula("산출식")
			.unit("단위")
			.taskTypes(createTaskTypeDtos())
			.serviceTargets(createGradeDtos())
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
}