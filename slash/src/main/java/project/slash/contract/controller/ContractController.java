package project.slash.contract.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.AllContractDto;
import project.slash.contract.dto.response.ContractDetailDto;
import project.slash.contract.dto.response.ContractNameDto;
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
	@PostMapping("/contract-manager/contract")
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
	@GetMapping("/common/contract/{contractId}")
	public BaseResponse<ContractDetailDto> showAllContractInfo(@PathVariable("contractId") Long contractId) {
		ContractDetailDto contractDetailDto = contractService.showAllContractInfo(contractId);

		return BaseResponse.ok(contractDetailDto);
	}

	/**
	 * 모든 계약 조회 메서드입니다.
	 *
	 * @return 모든 계약 정보
	 */
	@GetMapping("/contract-manager/all-contract")
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
	@DeleteMapping("/contract-manager/contract/{contractId}")
	public BaseResponse<Void> deleteContract(@PathVariable("contractId") Long contractId) {
		contractService.deleteContract(contractId);

		return BaseResponse.ok();
	}

	/**
	 * 협약서 이름 조회 메서드입니다.
	 * 
	 * @return 모든 협약서 이름
	 */
	@GetMapping("/common/all-contract-name")
	public BaseResponse<List<ContractNameDto>> showAllContractName() {
		List<ContractNameDto> contractNames = contractService.showAllContractName();

		return BaseResponse.ok(contractNames);
	}

	/**
	 * 종합 등급 수정하는 메서드입니다.
	 *
	 * @param contractId 수정할 계약 아이디
	 * @param gradeDtos 수정할 종합 등급 정보
	 * @return 성공 여부
	 */
	@PatchMapping("/contract-manager/total-target/{contractId}")
	public BaseResponse<Void> updateTotalTarget(@PathVariable("contractId") Long contractId, @RequestBody List<GradeDto> gradeDtos) {
		contractService.updateTotalTarget(contractId, gradeDtos);

		return BaseResponse.ok();
	}
}
