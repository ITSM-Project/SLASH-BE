package project.slash.security.auth.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.slash.security.auth.provider.JwtTokenProvider;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * [요청 시 거치는 필터 로직]
	 * Request는 다음 로직을 통과함.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;

		String requestURI = httpRequest.getRequestURI();

		// 로그인 요청 경로는 JWT 검증을 건너뜀
		if ("/login".equals(requestURI)) {
			chain.doFilter(request, response);
			return;
		}

		String token = jwtTokenProvider.resolveToken((HttpServletRequest)request);
		System.out.println(requestURI);
		System.out.println(token);
		try {
			jwtTokenProvider.validateToken(token);

			// 유효한 토큰일 경우 인증 객체 설정
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			chain.doFilter(request, response);
		} catch (IllegalArgumentException e) {
			log.error("JWT 검증 오류: {}", e.getMessage());
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.getWriter().write("Invalid JWT Token");
		}

	}

}
