package project.slash.security.auth.service;

import project.slash.security.auth.dto.JwtTokenDto;

public interface AuthService {
	JwtTokenDto login(String userId, String password);
}
