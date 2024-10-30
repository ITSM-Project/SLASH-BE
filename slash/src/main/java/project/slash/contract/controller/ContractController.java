package project.slash.contract.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	 * @return 저장된 계약 ID
	 */
	@PostMapping("/contract")
	public BaseResponse<Long> createContract(@RequestBody @Valid CreateContractDto createContractDto) {
		Long contractId = contractService.createContract(createContractDto);

		return BaseResponse.ok(contractId);
	}

	/**
	 * 계약 내용 조회 메서드입니다.
	 *
	 * @param contractId 조회 할 계약 아이디
	 * @return 게약 내용
	 */
	@GetMapping("/contract/{contractId}")
	public BaseResponse<ContractInfoDto> showContractInfo(@PathVariable("contractId") Long contractId) {
		ContractInfoDto contractInfoDto = contractService.showContractInfo(contractId);

		return BaseResponse.ok(contractInfoDto);
	}

	@DeleteMapping("/contract/{contractId}")
	public BaseResponse<Void> deleteContract(@PathVariable("contractId") Long contractId) {
		contractService.deleteContract(contractId);

		return BaseResponse.ok();
	}
}
