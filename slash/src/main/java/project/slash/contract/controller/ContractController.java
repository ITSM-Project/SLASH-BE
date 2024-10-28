package project.slash.contract.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.contract.dto.request.CreateContractDto;
import project.slash.contract.dto.response.ContractInfoDto;
import project.slash.contract.service.ContractService;

@RestController
@RequiredArgsConstructor
public class ContractController {

	private final ContractService contractService;

	/**
	 * 계약 생성 메서드입니다.
	 *
	 * @param createContractDto 계약 생성 정보
	 * @return 성공 여부
	 */
	@PostMapping("/contract")
	public BaseResponse<Void> createContract(@RequestBody @Valid CreateContractDto createContractDto) {
		contractService.createContract(createContractDto);

		return BaseResponse.ok();
	}

	/**
	 * 계약 내용 조회 메서드입니다.
	 *
	 * @return 계약 내용
	 */
	@GetMapping("/contract")
	public BaseResponse<ContractInfoDto> showContractInfo() {
		ContractInfoDto contractInfoDto = contractService.showContractInfo();

		return BaseResponse.ok(contractInfoDto);
	}
}
