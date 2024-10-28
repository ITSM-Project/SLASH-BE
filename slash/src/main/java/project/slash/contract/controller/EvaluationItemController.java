package project.slash.contract.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.contract.dto.request.DetailDto;
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
	public BaseResponse<?> createDetail(@RequestBody @Valid DetailDto detailDto) {
		evaluationItemService.createDetail(detailDto);

		return BaseResponse.ok();
	}
}
