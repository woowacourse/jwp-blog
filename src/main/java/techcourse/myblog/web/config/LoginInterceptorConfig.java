package techcourse.myblog.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.interceptor.LoginInterceptor;

@Configuration
public class LoginInterceptorConfig implements WebMvcConfigurer {
    private final HandlerInterceptor loginInterceptor;

    public LoginInterceptorConfig(final LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/mypage/**")
                .addPathPatterns("/logout")
                .addPathPatterns("/articles/**")
                .addPathPatterns("/users/*");
    }
}