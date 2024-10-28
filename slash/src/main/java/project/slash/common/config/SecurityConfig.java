package project.slash.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import project.slash.security.auth.CustomUserDetailService;
import project.slash.security.auth.UserAuthFailureHandler;
import project.slash.security.auth.UserAuthSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailService customUserDetailsService;
	private final UserAuthSuccessHandler successHandler;
	private final UserAuthFailureHandler failureHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(requests -> requests
				.requestMatchers("/login", "/error").permitAll()
				.requestMatchers("/contract-admin/**").hasRole("CONTRACT_ADMIN")
				.requestMatchers("/service-admin/**").hasRole("SERVICE_ADMIN")
				.requestMatchers("/user/**").hasRole("USER")
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login") // 로그인 페이지 사용 - 추후 프론트 연결
				.loginProcessingUrl("/login")
				.usernameParameter("id")
				.passwordParameter("password")
				.successHandler(successHandler)  // 로그인 성공 핸들러
				.failureHandler(failureHandler)  // 로그인 실패 핸들러
				.permitAll()
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
			);
		return http.build();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		builder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
		return builder.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
