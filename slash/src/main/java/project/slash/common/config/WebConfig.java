package project.slash.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import project.slash.security.annotation.LoginArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final LoginArgumentResolver loginArgumentResolver;

	public WebConfig(LoginArgumentResolver loginArgumentResolver) {
		this.loginArgumentResolver = loginArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginArgumentResolver);
	}
}
