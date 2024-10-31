package project.slash.common.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import project.slash.security.auth.CustomUserDetailService;
import project.slash.security.auth.UserAuthFailureHandler;
import project.slash.security.auth.UserAuthSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	private final ObjectMapper objectMapper;
	private final CustomUserDetailService customUserDetailsService;
	private final UserAuthSuccessHandler successHandler;
	private final UserAuthFailureHandler failureHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
		AuthenticationManager authenticationManager) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(requests -> requests
				.requestMatchers("/login", "/logout", "/error").permitAll()
				.requestMatchers("/requestManager/**").hasRole("REQUEST_MANAGER")
				.requestMatchers("/contractManager/**").hasRole("CONTRACT_MANAGER")
				.requestMatchers("/user/**").hasRole("USER")
				.anyRequest().authenticated()
			)
			.formLogin(AbstractHttpConfigurer::disable
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
				.invalidateHttpSession(true)  // 세션 무효화
				.deleteCookies("JSESSIONID")  // 쿠키 삭제
				.permitAll()
			)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 필요 시 세션 생성
				.sessionFixation(
					SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)// 세션 ID 변경 전략 명시
				.maximumSessions(1)  // 최대 허용 세션 개수 1개
				.maxSessionsPreventsLogin(true)  // 기존 세션이 있으면 새로운 로그인 차단
				.sessionRegistry(sessionRegistry())  // 세션 레지스트리 등록
			)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));  // 허용할 클라이언트 도메인
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
		throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
