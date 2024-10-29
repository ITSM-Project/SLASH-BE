package project.slash.security.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper;
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	public JsonAuthenticationFilter(
		String loginUrl,
		ObjectMapper objectMapper,
		UserDetailsService userDetailsService,
		PasswordEncoder passwordEncoder) {
		super(loginUrl);  // 필터가 처리할 URL 경로 설정
		this.objectMapper = objectMapper;
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {
		System.out.println("attemptAuthentication");
		if (!isJsonRequest(request)) {
			throw new AuthenticationServiceException("Invalid Content-Type: " + request.getContentType());
		}

		try {
			Map<String, String> loginData = objectMapper.readValue(
				StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), Map.class);

			String username = loginData.get("id");
			String password = loginData.get("password");
			System.out.println("username: " + username + " password: " + password);
			if (username == null || password == null) {
				throw new AuthenticationServiceException("Missing username or password");
			}

			var userDetails = userDetailsService.loadUserByUsername(username);

			if (!passwordEncoder.matches(password, userDetails.getPassword())) {
				throw new AuthenticationServiceException("Invalid credentials");
			}

			UsernamePasswordAuthenticationToken authRequest =
				new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

			setDetails(request, authRequest);
			return this.getAuthenticationManager().authenticate(authRequest);

		} catch (IOException e) {
			throw new AuthenticationServiceException("Failed to parse JSON request", e);
		}
	}

	private boolean isJsonRequest(HttpServletRequest request) {
		String contentType = request.getContentType();
		return contentType != null && contentType.equalsIgnoreCase("application/json");
	}

	// 인증 객체에 세부 정보 설정
	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}
}
