package techcourse.myblog.config;

import java.util.List;

import techcourse.myblog.custom.LoginUserResolver;
import techcourse.myblog.interceptor.AuthenticationInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
				.excludePathPatterns("/")
				.excludePathPatterns("/css/**")
				.excludePathPatterns("/js/**")
				.excludePathPatterns("/images/**");
	}

	@Bean
	public LoginUserResolver loginUserResolver() {
		return new LoginUserResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginUserResolver());
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
	}
}
