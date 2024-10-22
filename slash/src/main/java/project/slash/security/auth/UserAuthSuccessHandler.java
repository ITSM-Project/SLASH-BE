package project.slash.security.auth;

import java.io.IOException;
import java.util.Collection;

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

		// 역할에 따라 리다이렉트할 URL 결정
		String redirectUrl = determineRedirectUrl(authorities);

		// 지정된 URL로 리다이렉트
		response.sendRedirect(redirectUrl);
	}

	private String determineRedirectUrl(Collection<? extends GrantedAuthority> authorities) {
		for (GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();
			if (role.equals("ROLE_USER")) {
				return "/user/dashboard";
			} else if (role.equals("ROLE_CONTRACT_ADMIN")) {
				return "/contract-admin/dashboard";
			} else if (role.equals("ROLE_SERVICE_ADMIN")) {
				return "/service-admin/dashboard";
			}
		}
		// 기본 리다이렉트 경로 (예: 권한 없는 사용자)
		return "/";
	}
}
