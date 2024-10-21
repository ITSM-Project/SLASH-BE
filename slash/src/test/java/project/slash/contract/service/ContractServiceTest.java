package project.slash.contract.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.slash.contract.dto.request.CreateContractDto;

@SpringBootTest
@ActiveProfiles("test")
class ContractServiceTest {
	@Autowired
	private ContractService contractService;
}
