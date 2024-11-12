package project.slash.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.common.response.BaseResponse;
import project.slash.security.auth.dto.JwtTokenDto;
import project.slash.security.auth.service.AuthService;
import project.slash.security.auth.dto.LoginRequestDto;

@RequiredArgsConstructor
@RestController
public class UserController {
	private final AuthService authService;

	/**
	 * 로그인 하는 메서드 입니다.
	 *
	 * @return 토큰 정보
	 */

	@PostMapping("/login")
	public BaseResponse<JwtTokenDto> login(@RequestBody LoginRequestDto userLoginRequestDto) {
		String userId = userLoginRequestDto.getId();
		String password = userLoginRequestDto.getPassword();
		JwtTokenDto tokenInfo = authService.login(userId, password);
		return BaseResponse.ok(tokenInfo);
	}
}
