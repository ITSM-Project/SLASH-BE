// package project.slash.taskrequest.repository;
//
// import static org.assertj.core.api.Assertions.*;
//
// import java.time.LocalDateTime;
// import java.util.List;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
//
// import project.slash.system.model.Equipment;
// import project.slash.system.repository.EquipmentRepository;
// import project.slash.taskrequest.dto.request.TaskRequestResponseDTO;
// import project.slash.taskrequest.model.TaskRequest;
// import project.slash.taskrequest.model.TaskType;
// import project.slash.taskrequest.model.constant.RequestStatus;
// import project.slash.user.model.User;
// import project.slash.user.repository.UserRepository;
//
// @SpringBootTest
// @ActiveProfiles("test")
// public class TaskRequestRepositoryTest {
//
// 	@Autowired
// 	private TaskRequestRepository taskRequestRepository;
//
// 	@Autowired
// 	private EquipmentRepository equipmentRepository;
//
// 	@Autowired
// 	private TaskTypeRepository taskTypeRepository;
//
// 	@Autowired
// 	private UserRepository userRepository;
//
// 	@BeforeEach
// 	public void setUp() {
// 		// 1. User 생성 및 저장 (요청자와 담당자)
// 		User requester = User.builder()
// 			.id("requesterId")
// 			.role("REQUESTER_ROLE")
// 			.password("password")
// 			.email("requester@example.com")
// 			.phoneNum("010-1234-5678")
// 			.name("Requester Name")
// 			.build();
//
// 		User manager = User.builder()
// 			.id("managerId")
// 			.role("MANAGER_ROLE")
// 			.password("password")
// 			.email("manager@example.com")
// 			.phoneNum("010-8765-4321")
// 			.name("Manager Name")
// 			.build();
//
// 		userRepository.save(requester);
// 		userRepository.save(manager);
//
// 		// 2. Equipment 생성 및 저장
// 		Equipment equipment = Equipment.builder()
// 			.name("Server")
// 			.build();
// 		equipmentRepository.save(equipment);
//
// 		// 3. TaskType 생성 및 저장
// 		TaskType taskType = TaskType.builder()
// 			.taskType("Maintenance")
// 			.taskDetail("Routine Check")
// 			.build();
// 		taskTypeRepository.save(taskType);
//
// 		// 4. TaskRequest 생성 및 저장
// 		TaskRequest taskRequest = TaskRequest.builder()
// 			.title("Test Request")
// 			.content("This is a test task request")
// 			.status(RequestStatus.REGISTERED)
// 			.taskType(taskType)
// 			.requester(requester)
// 			.manager(manager)
// 			.equipment(equipment)
// 			.requestTime(LocalDateTime.now())
// 			.completionTime(LocalDateTime.now().plusDays(1))
// 			.dueOnTime(true)
// 			.build();
// 		taskRequestRepository.save(taskRequest);
// 	}
//
// 	@Test
// 	public void testFindFilteredRequests_withAllFilters() {
// 		// when: 필터 조건을 모두 설정하고 쿼리 실행
// 		List<TaskRequestResponseDTO> results = taskRequestRepository.findFilteredRequests("Server", "Routine Check", RequestStatus.REGISTERED);
//
// 		// then: 결과 검증
// 		assertThat(results).isNotEmpty();
// 		TaskRequestResponseDTO result = results.get(0);
//
// 		// 필드 값 검증
// 		assertThat(result.getRequesterName()).isEqualTo("Requester Name");
// 		assertThat(result.getManagerName()).isEqualTo("Manager Name");
// 		assertThat(result.getEquipmentType()).isEqualTo("Server");
// 		assertThat(result.getTaskType()).isEqualTo("Maintenance");
// 		assertThat(result.getTaskDetail()).isEqualTo("Routine Check");
// 		assertThat(result.getTitle()).isEqualTo("Test Request");
// 		assertThat(result.getContent()).isEqualTo("This is a test task request");
// 		assertThat(result.getRequestTime()).isNotNull();
// 		assertThat(result.getCompletionTime()).isNotNull();
// 		assertThat(result.isDueOnTime()).isTrue();
// 		assertThat(result.getStatus()).isEqualTo(RequestStatus.REGISTERED);
// 	}
//
// 	@Test
// 	public void testFindFilteredRequests_withPartialFilters() {
// 		// when: 일부 필터 조건만 설정하고 쿼리 실행
// 		List<TaskRequestResponseDTO> results = taskRequestRepository.findFilteredRequests("Server", null, null);
//
// 		// then: 결과 검증
// 		assertThat(results).isNotEmpty();
// 		TaskRequestResponseDTO result = results.get(0);
//
// 		// 필드 값 검증
// 		assertThat(result.getEquipmentType()).isEqualTo("Server");
// 		assertThat(result.getTaskType()).isEqualTo("Maintenance");
// 		assertThat(result.getStatus()).isEqualTo(RequestStatus.REGISTERED);
// 	}
//
// 	@Test
// 	public void testFindFilteredRequests_withoutFilters() {
// 		// when: 필터 조건을 설정하지 않고 전체 조회
// 		List<TaskRequestResponseDTO> results = taskRequestRepository.findFilteredRequests(null, null, null);
//
// 		// then: 전체 데이터가 조회되는지 검증
// 		assertThat(results).isNotEmpty();
// 		TaskRequestResponseDTO result = results.get(0);
//
// 		// 필드 값 검증
// 		assertThat(result.getRequesterName()).isEqualTo("Requester Name");
// 		assertThat(result.getManagerName()).isEqualTo("Manager Name");
// 		assertThat(result.getEquipmentType()).isEqualTo("Server");
// 		assertThat(result.getTaskType()).isEqualTo("Maintenance");
// 		assertThat(result.getTaskDetail()).isEqualTo("Routine Check");
// 		assertThat(result.getTitle()).isEqualTo("Test Request");
// 		assertThat(result.getContent()).isEqualTo("This is a test task request");
// 		assertThat(result.getRequestTime()).isNotNull();
// 		assertThat(result.getCompletionTime()).isNotNull();
// 		assertThat(result.isDueOnTime()).isTrue();
// 		assertThat(result.getStatus()).isEqualTo(RequestStatus.REGISTERED);
// 	}
// }
