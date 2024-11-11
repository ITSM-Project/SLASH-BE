package project.slash.contract.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContractNameDto {
	private Long contractId;
	private String contractName;

	public static ContractNameDto of(Long contractId, String contractName) {
		return new ContractNameDto(contractId, contractName);
	}
}
