package project.slash.contract.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.contract.dto.request.CreateEvaluationItemDto;
import project.slash.contract.dto.response.DefaultEvaluationItemDto;
import project.slash.contract.dto.response.EvaluationItemCategoryDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.service.EvaluationItemService;

@RequiredArgsConstructor
@RestController
public class EvaluationItemController {
	private final EvaluationItemService evaluationItemService;

	/**
	 * 서비스 항목 세부 내용 설정 메서드입니다.
	 *
	 * @param createEvaluationItemDto 세부 내용 정보
	 * @return 성공 여부
	 */
	@PostMapping("/contract-manager/evaluation-item")
	public BaseResponse<Void> createEvaluationItem(@RequestBody @Valid CreateEvaluationItemDto createEvaluationItemDto) {
		evaluationItemService.createEvaluationItem(createEvaluationItemDto);

		return BaseResponse.ok();
	}

	/**
	 * 서비스 항목 별 세부 내용 조회 메서드
	 *
	 * @param evaluationItemId 서비스 항목 ID
	 * @return 서비스 항목 세부 내용(서비스 목표, 업무 유형, 서비스 항목 설명)
	 */
	@GetMapping("/common/detail/{id}")
	public BaseResponse<EvaluationItemDetailDto> showCategoryDetail(@PathVariable("id") Long evaluationItemId) {
		EvaluationItemDetailDto evaluationItemDetail = evaluationItemService.findDetailByItemId(evaluationItemId);

		return BaseResponse.ok(evaluationItemDetail);
	}

	/**
	 * 계약 내용 수정 가능 여부 조회 메서드
	 *
	 * @param contractId 조회 할 계약 아이디
	 * @return 수정 가능 여부
	 */
	@GetMapping("/contract-manager/modifiable/{contractId}")
	public BaseResponse<Boolean> checkModifiable(@PathVariable("contractId") Long contractId) {
		boolean modifiable = evaluationItemService.checkModifiable(contractId);

		return BaseResponse.ok(modifiable);
	}

	/**
	 * 서비스 평가 항목 수정 메서드입니다.
	 *
	 * @param evaluationItemId 수정할 서비스 평가 항목 아이디
	 * @param evaluationItemDto 수정할 내용
	 * @return 성공 여부
	 */
	@PutMapping("/contract-manager/evaluation-item/{id}")
	public BaseResponse<Void> updateEvaluationItem(@PathVariable("id") Long evaluationItemId,
		@RequestBody CreateEvaluationItemDto evaluationItemDto) {
		evaluationItemService.updateEvaluationItem(evaluationItemId, evaluationItemDto);

		return BaseResponse.ok();
	}

	/**
	 * 변경된 서비스 평가 항목 생성 메서드입니다.(기존 서비스 평가 항목 비활성화)
	 *
	 * @param evaluationItemId 수정할 서비스 평가 항목
	 * @param evaluationItemDto 수정 내용
	 * @return 성공 여부
	 */
	@PostMapping("/contract-manager/evaluation-item/{id}")
	public BaseResponse<Void> newEvaluationItem(@PathVariable("id") Long evaluationItemId, @RequestBody CreateEvaluationItemDto evaluationItemDto) {
		evaluationItemService.newEvaluationItem(evaluationItemId, evaluationItemDto);

		return BaseResponse.ok();
	}

	/**
	 * 서비스 평가 항목 이름 조회 메서드입니다.
	 *
	 * @param contractId 조회할 계약 아이디
	 * @return 서비스 평가 항목 이름
	 */
	@GetMapping("/common/evaluation-item-category/{contractId}")
	public BaseResponse<List<EvaluationItemCategoryDto>> getEvaluationItemCategory(@PathVariable Long contractId) {
		List<EvaluationItemCategoryDto> evaluationItemCategories = evaluationItemService.getEvaluationItemCategory(
			contractId);

		return BaseResponse.ok(evaluationItemCategories);
	}

	/**
	 * 서비스 평가 항목 삭제 메서드입니다.
	 *
	 * @param evaluationItemId 삭제할 서비스 평가 항목 아이디
	 * @return 성공 여부
	 */
	@DeleteMapping("/contract-manager/evaluation-item/{id}")
	public BaseResponse<Void> deleteEvaluationItem(@PathVariable("id") Long evaluationItemId) {
		evaluationItemService.deleteEvaluationItem(evaluationItemId);

		return BaseResponse.ok();
	}

	/**
	 * 기본으로 저장된 서비스 평가항목 내용 조회 메서드
	 *
	 * @param category 서비스 평가 항목 이름
	 * @return 서비스 평가 항목 내용
	 */
	@GetMapping("/contract-manager/evaluation-item/{category}")
	public BaseResponse<DefaultEvaluationItemDto> fetchEvaluationItem(@PathVariable("category") String category) {
		DefaultEvaluationItemDto defaultEvaluationItemDto = evaluationItemService.fetchEvaluationItem(category);

		return BaseResponse.ok(defaultEvaluationItemDto);
	}
}
