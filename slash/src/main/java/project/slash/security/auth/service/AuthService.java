package project.slash.security.auth.service;

import project.slash.security.auth.dto.JwtTokenDTO;

public interface AuthService {
	JwtTokenDTO login(String userId, String password);
}
