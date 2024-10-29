package project.slash.contract.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.contract.dto.request.CreateDetailDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.service.EvaluationItemService;

@RequiredArgsConstructor
@RestController
public class EvaluationItemController {
	private final EvaluationItemService evaluationItemService;

	/**
	 * 서비스 항목 세부 내용 설정 메서드입니다.
	 *
	 * @param detailDto 세부 내용 정보
	 * @return 성공 여부
	 */
	@PostMapping("/detail")
	public BaseResponse<Void> createDetail(@RequestBody @Valid CreateDetailDto detailDto) {
		evaluationItemService.createDetail(detailDto);

		return BaseResponse.ok();
	}

	/**
	 * 서비스 항목 별 세부 내용 조회 메서드
	 *
	 * @param categoryId 서비스 항목 아이디
	 * @return 서비스 항목 세부 내용(서비스 목표, 업무 유형, 서비스 항목 설명)
	 */
	@GetMapping("/detail/{categoryId}")
	public BaseResponse<EvaluationItemDetailDto> showCategoryDetail(@PathVariable("categoryId") Long categoryId) {
		EvaluationItemDetailDto evaluationItemDetail = evaluationItemService.findDetailByCategoryId(categoryId);

		return BaseResponse.ok(evaluationItemDetail);
	}
}
