// package project.slash.system.repository;
//
// import static org.assertj.core.api.Assertions.*;
//
// import java.util.List;
//
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
//
// import project.slash.system.dto.response.AllSystemsInfo;
// import project.slash.system.model.Equipment;
// import project.slash.system.model.Systems;
//
// @SpringBootTest
// @ActiveProfiles("test")
// class SystemsRepositoryTest {
// 	@Autowired
// 	private EquipmentRepository equipmentRepository;
// 	@Autowired
// 	private SystemsRepository systemsRepository;
//
// 	@AfterEach
// 	void tearDown() {
// 		equipmentRepository.deleteAllInBatch();
// 		systemsRepository.deleteAllInBatch();
// 	}
//
// 	@DisplayName("가지고 있는 시스템과 장비를 전부 조회할 수 있다.")
// 	@Test
// 	void findAllSystemsWithEquipments() {
// 		//given
// 		Systems system1 = systemsRepository.save(Systems.of("서버"));
// 		Systems system2 = systemsRepository.save(Systems.of("DB"));
//
// 		Equipment server1 = Equipment.from("서버1", system1);
// 		Equipment server2 = Equipment.from("서버2", system1);
// 		Equipment db1 = Equipment.from("DB1", system2);
//
// 		equipmentRepository.saveAll(List.of(server1, server2, db1));
//
// 		//when
// 		List<AllSystemsInfo> result = systemsRepository.findAllSystemsWithEquipments();
//
// 		//then
// 		assertThat(result).hasSize(2)
// 			.extracting(
// 				AllSystemsInfo::getSystemName,
// 				r -> r.getEquipmentInfos().size()
// 			)
// 			.containsExactlyInAnyOrder(
// 				tuple("서버", 2),
// 				tuple("DB", 1)
// 			);
// 	}
// }
