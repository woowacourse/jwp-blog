package techcourse.myblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.LoginInterceptor;
import techcourse.myblog.web.Constants;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

	private final LoginInterceptor loginInterceptor;

	@Autowired
	public MvcConfigurer(final LoginInterceptor loginInterceptor) {
		this.loginInterceptor = loginInterceptor;
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns(Constants.LOGIN_URL)
				.addPathPatterns(Constants.SIGNUP_URL);
	}
}
