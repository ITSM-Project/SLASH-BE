package project.slash.contract.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.AllContractDto;
import project.slash.contract.dto.response.ContractInfoDto;
import project.slash.contract.service.ContractService;

@RestController
@RequiredArgsConstructor
public class ContractController {

	private final ContractService contractService;

	/**
	 * 계약 생성 메서드입니다.
	 *
	 * @param contractRequestDto 계약 생성 정보
	 * @return 저장된 계약 ID
	 */
	@PostMapping("/contract")
	public BaseResponse<Long> createContract(@RequestBody @Valid ContractRequestDto contractRequestDto) {
		Long contractId = contractService.createContract(contractRequestDto);

		return BaseResponse.ok(contractId);
	}

	/**
	 * 특정 계약 내용 조회 메서드입니다.
	 *
	 * @param contractId 조회 할 계약 ID
	 * @return 게약 내용
	 */
	@GetMapping("/contract/{contractId}")
	public BaseResponse<ContractInfoDto> showContractInfo(@PathVariable("contractId") Long contractId) {
		ContractInfoDto contractInfoDto = contractService.showContractInfo(contractId);

		return BaseResponse.ok(contractInfoDto);
	}

	/**
	 * 모든 계약 조회 메서드입니다.
	 *
	 * @return 모든 계약 정보
	 */
	@GetMapping("/all-contract")
	public BaseResponse<List<AllContractDto>> showAllContract() {
		List<AllContractDto> allContracts = contractService.showAllContract();

		return BaseResponse.ok(allContracts);
	}

	/**
	 * 계약 삭제 메서드입니다.
	 *
	 * @param contractId 삭제할 계약 ID
	 * @return 성공 여부
	 */
	@DeleteMapping("/contract/{contractId}")
	public BaseResponse<Void> deleteContract(@PathVariable("contractId") Long contractId) {
		contractService.deleteContract(contractId);

		return BaseResponse.ok();
	}
}
