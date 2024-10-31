package project.slash.contract.service;

import static project.slash.contract.exception.ContractErrorCode.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.slash.common.exception.BusinessException;
import project.slash.contract.dto.request.ContractRequestDto;
import project.slash.contract.dto.response.AllContractDto;
import project.slash.contract.dto.response.ContractDto;
import project.slash.contract.dto.response.ContractInfoDto;
import project.slash.contract.dto.response.PreviewEvaluationItemDto;
import project.slash.contract.model.Contract;
import project.slash.contract.model.TotalTarget;
import project.slash.contract.repository.ContractRepository;
import project.slash.contract.repository.TotalTargetRepository;
import project.slash.evaluationitem.repository.EvaluationItemRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractService {
	private final TotalTargetRepository totalTargetRepository;
	private final ContractRepository contractRepository;
	private final EvaluationItemRepository evaluationItemRepository;

	@Transactional
	public Long createContract(ContractRequestDto contractRequestDto) {
		//TODO: 만료되지 않은 계약에 대해서 2개 이상 생성 불가능 하도록 해야함
		Contract contract = contractRepository.save(Contract.from(contractRequestDto));    //계약 저장

		List<TotalTarget> totalTargets = contractRequestDto.getTotalTargets().stream()
			.map(target -> TotalTarget.from(target, contract)).toList();    //종합 평가 등급 저장

		totalTargetRepository.saveAll(totalTargets);
		return contract.getId();
	}

	public ContractInfoDto showContractInfo(Long contractId) {
		ContractDto contractDto = contractRepository.findContractById(contractId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_CONTRACT));

		List<PreviewEvaluationItemDto> evaluationItems = evaluationItemRepository.findEvaluationItem(contractId);
		return ContractInfoDto.of(contractId, contractDto, evaluationItems);
	}

	@Transactional
	public void deleteContract(Long contractId) {
		Contract contract = findContract(contractId);

		if(LocalDate.now().isAfter(contract.getEndDate())){
			throw new BusinessException(NOT_TERMINATE_CONTRACT);
		}

		contract.updateTerminateStatus();
	}

	public List<AllContractDto> showAllContract() {
		List<Contract> allContracts = contractRepository.findAllByOrderByStartDateDesc();

		return allContracts.stream()
			.map(AllContractDto::from).toList();
	}

	@Transactional
	public void editContract(Long contractId, ContractRequestDto contractRequestDto) {
		Contract contract = findContract(contractId);
		contract.update(contractRequestDto.getCompanyName(), contract.getStartDate(), contract.getEndDate());

		if(!contractRequestDto.getTotalTargets().isEmpty()) {
			updateTotalTargets(contractRequestDto, contract);
		}
	}

	private void updateTotalTargets(ContractRequestDto contractRequestDto, Contract contract) {
		//기존 데이터 삭제
		List<TotalTarget> totalTargets = totalTargetRepository.findByContractId(contract.getId());
		totalTargetRepository.deleteAll(totalTargets);

		//새로은 데이터 추가
		List<TotalTarget> newTotalTargets = contractRequestDto.getTotalTargets().stream()
			.map(target -> TotalTarget.from(target, contract)).toList();
		totalTargetRepository.saveAll(newTotalTargets);
	}

	private Contract findContract(Long contractId) {
		return contractRepository.findById(contractId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_CONTRACT));
	}
}
