package techcourse.myblog.config;

import java.util.Arrays;
import java.util.List;

import techcourse.myblog.interceptor.AuthenticationInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private List<String> url = Arrays.asList("/login", "/signup", "/users", "/user-list", "/leave",
			"/edit", "/mypage", "/mypage/edit", "/articles");

	@Bean
	public AuthenticationInterceptor basicAuthInterceptor() {
		return new AuthenticationInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(basicAuthInterceptor())
				.addPathPatterns(url);
	}
}
