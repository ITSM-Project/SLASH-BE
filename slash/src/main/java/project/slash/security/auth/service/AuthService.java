package project.slash.security.auth.service;

import project.slash.security.auth.DTO.JwtTokenDTO;

public interface AuthService {
	JwtTokenDTO login(String userId, String password);
}
