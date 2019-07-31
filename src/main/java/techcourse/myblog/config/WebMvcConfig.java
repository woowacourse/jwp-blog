package techcourse.myblog.config;

import techcourse.myblog.interceptor.AuthenticationInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Bean
	public AuthenticationInterceptor basicAuthInterceptor() {
		return new AuthenticationInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(basicAuthInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/");
	}
}
