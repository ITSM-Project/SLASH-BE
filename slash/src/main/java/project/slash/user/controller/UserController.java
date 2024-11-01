package project.slash.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.security.auth.dto.JwtTokenDTO;
import project.slash.security.auth.service.AuthService;
import project.slash.security.auth.dto.LoginRequestDTO;

@RequiredArgsConstructor
@RestController
public class UserController {
	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<JwtTokenDTO> login(@RequestBody LoginRequestDTO userLoginRequestDto) {
		String userId = userLoginRequestDto.getId();
		String password = userLoginRequestDto.getPassword();
		JwtTokenDTO tokenInfo = authService.login(userId, password);
		return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);
	}

	@GetMapping("contractManager/dashboard")
	public String contractManagerDashboard() {
		return "contractManager/dashboard";
	}

	@GetMapping("/serviceManager/dashboard")
	public String serviceManagerDashboard() {
		return "serviceManager/dashboard";
	}

	@GetMapping("/user/dashboard")
	public String userDashboard() {
		return "user/dashboard";
	}

}
