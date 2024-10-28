package project.slash.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import project.slash.security.auth.CustomUserDetailService;
import project.slash.security.auth.UserAuthFailureHandler;
import project.slash.security.auth.UserAuthSuccessHandler;

@Configuration
public class SecurityConfig {

	private final CustomUserDetailService customUserDetailsService;
	private final UserAuthSuccessHandler successHandler;
	private final UserAuthFailureHandler failureHandler;

	public SecurityConfig(CustomUserDetailService customUserDetailsService, UserAuthSuccessHandler successHandler,
		UserAuthFailureHandler failureHandler) {
		this.customUserDetailsService = customUserDetailsService;
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf->csrf.disable())
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/","/login","/perform_login","/home", "/error").permitAll()
				.requestMatchers("/contract-admin/**").hasRole("CONTRACT_ADMIN")
				.requestMatchers("/service-admin/**").hasRole("SERVICE_ADMIN")
				.requestMatchers("/user/**").hasRole("USER")
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login") // 로그인 페이지 사용 - 추후 프론트 연결
				.loginProcessingUrl("/perform_login") // 로그인 기능을 실행 - 시큐리티 제공.
				.usernameParameter("id")
				.passwordParameter("password")
				.successHandler(successHandler)  // 로그인 성공 핸들러
				.failureHandler(failureHandler)  // 로그인 실패 핸들러
				.permitAll()
			)
			.logout((logout) -> logout
				.logoutUrl("/perform_logout")
				.logoutSuccessUrl("/login")
				.permitAll());

		return http.build();
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
