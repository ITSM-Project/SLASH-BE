package project.slash.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import project.slash.user.model.User;
import project.slash.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// 테스트용 데이터 자동 생성
	@PostConstruct
	public void initCustomsData() {
		List<User> customs = List.of(
			User.from("1", "ROLE_CONTRACT_ADMIN", "a", passwordEncoder.encode("1234"),
				"test1@test.com", "010-1234-1111"),
			User.from("2", "ROLE_SERVICE_ADMIN", "b", passwordEncoder.encode("1234"),
				"test2@test.com", "010-1234-2222"),
			User.from("3", "ROLE_USER", "c", passwordEncoder.encode("1234"),
				"test3@test.com", "010-1234-3333")
		);

		userRepository.saveAll(customs);
	}
}
