package project.slash.system.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.slash.system.dto.response.AllSystemsInfo;

@SpringBootTest
@ActiveProfiles("test")
class SystemsRepositoryTest {
	@Autowired
	private EquipmentRepository equipmentRepository;
	@Autowired
	private SystemsRepository systemsRepository;

	@AfterEach
	void tearDown() {
		equipmentRepository.deleteAllInBatch();
		systemsRepository.deleteAllInBatch();
	}

	@DisplayName("가지고 있는 시스템과 장비를 전부 조회할 수 있다.")
	@Test
	void findAllSystemsWithEquipments() {
		//given //when
		List<AllSystemsInfo> result = systemsRepository.findAllSystemsWithEquipments();

		//then
		assertThat(result).hasSize(4)
			.extracting(
				AllSystemsInfo::getSystemName,
				r -> r.getEquipmentInfos().size()
			)
			.containsExactlyInAnyOrder(
				tuple("서버", 5),
				tuple("DB", 5),
				tuple("백업", 5),
				tuple("응용프로그램", 5)
			);
	}
}
