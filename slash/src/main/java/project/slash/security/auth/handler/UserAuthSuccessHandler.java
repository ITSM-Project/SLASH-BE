package project.slash.security.auth.handler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		// 사용자의 권한 정보 가져오기
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		// 역할에 따라 이동할 경로 결정
		String targetUrl = determineRedirectUrl(authorities);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// 성공시 redirectUrl 반환
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("message", "로그인 성공");
		responseBody.put("redirectUrl", targetUrl);

		response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(responseBody));
		response.getWriter().flush();
	}

	private String determineRedirectUrl(Collection<? extends GrantedAuthority> authorities) {
		for (GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();
			switch (role) {
				case "ROLE_USER" -> {
					return "user";
				}
				case "ROLE_REQUEST_MANAGER" -> {
					return "requestManager";
				}
				case "ROLE_CONTRACT_MANAGER" -> {
					return "contractManager";
				}
			}
		}
		// 기본 경로
		return "no-role";
	}
}
