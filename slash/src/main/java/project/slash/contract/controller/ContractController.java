package project.slash.contract.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.contract.dto.request.CreateContractDto;
import project.slash.contract.service.ContractService;

@RestController
@RequiredArgsConstructor
public class ContractController {

	private final ContractService contractService;

	@PostMapping("/contract")
	public BaseResponse<Void> createContract(@RequestBody @Valid CreateContractDto createContractDto) {
		contractService.createContract(createContractDto);

		return BaseResponse.ok();
	}
}
