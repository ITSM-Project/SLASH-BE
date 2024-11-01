package project.slash.security.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserAuthFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// 실패 응답 데이터 생성
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("error", "인증 실패");
		responseBody.put("message", exception.getMessage());

		// JSON 형식으로 응답 전송
		response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(responseBody));
		response.getWriter().flush();
	}
}
