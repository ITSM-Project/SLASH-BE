// package project.slash.common.config;
//
// import java.util.Collections;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.session.SessionRegistry;
// import org.springframework.security.core.session.SessionRegistryImpl;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
// import lombok.RequiredArgsConstructor;
// import project.slash.security.auth.filter.JwtAuthenticationFilter;
// import project.slash.security.auth.provider.JwtTokenProvider;
//
// @Configuration
// @RequiredArgsConstructor
// @EnableWebSecurity
// public class SecurityConfig {
// 	private final JwtTokenProvider jwtTokenProvider;
//
// 	@Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// 		http
// 			.httpBasic(AbstractHttpConfigurer::disable)
// 			.csrf(AbstractHttpConfigurer::disable)
// 			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
// 			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// 			.formLogin(AbstractHttpConfigurer::disable
// 			)
// 			.authorizeHttpRequests(requests -> requests
// 				.requestMatchers("/", "/login", "/logout", "/error").permitAll()
// 				.requestMatchers("/request-manager/**").hasRole("REQUEST_MANAGER")
// 				.requestMatchers("/contract-manager/**").hasRole("CONTRACT_MANAGER")
// 				.requestMatchers("/user/**").hasRole("USER")
// 				.anyRequest().authenticated()
// 			)
// 			.logout(logout -> logout
// 				.logoutUrl("/logout")
// 				.logoutSuccessUrl("/")
// 				.permitAll()
// 			)
// 			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
// 		return http.build();
// 	}
//
// 	@Bean
// 	public CorsConfigurationSource corsConfigurationSource() {
// 		CorsConfiguration config = new CorsConfiguration();
// 		config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));  // 허용할 클라이언트 도메인
// 		config.setAllowedMethods(Collections.singletonList("*"));
// 		config.setAllowedHeaders(Collections.singletonList("*"));
// 		config.setAllowCredentials(true);
// 		config.setMaxAge(3600L);
//
// 		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
// 		source.registerCorsConfiguration("/**", config);
// 		return source;
// 	}
//
// 	@Bean
// 	public SessionRegistry sessionRegistry() {
// 		return new SessionRegistryImpl();
// 	}
//
// 	@Bean
// 	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
// 		throws Exception {
// 		return authenticationConfiguration.getAuthenticationManager();
// 	}
//
// 	@Bean
// 	public PasswordEncoder passwordEncoder() {
// 		return new BCryptPasswordEncoder();
// 	}
//
// }
package project.slash.common.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정 적용
			.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());  // 모든 요청 허용
		return http.build();  // 반환 타입 수정
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 허용할 도메인
		config.setAllowedMethods(Collections.singletonList("*")); // 모든 메서드 허용
		config.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
