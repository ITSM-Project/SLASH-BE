package project.slash.contract.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.contract.dto.request.CreateContractDto;
import project.slash.contract.service.ContractService;

@RestController
@RequiredArgsConstructor
public class ContractController {

	private final ContractService contractService;

	@PostMapping("/contract")
	public BaseResponse<Void> createContract(@RequestBody CreateContractDto createContractDto) {
		contractService.createContract(createContractDto);

		return BaseResponse.ok();
	}
}
