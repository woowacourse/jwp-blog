package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.presentation.interceptor.LoginInterceptor;
import techcourse.myblog.presentation.interceptor.LogoutInterceptor;
import techcourse.myblog.presentation.support.LoginUserHandlerMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final HandlerInterceptor loginInterceptor;
    private final HandlerInterceptor logoutInterceptor;

    public WebMvcConfig(final LoginInterceptor loginInterceptor, final LogoutInterceptor logoutInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.logoutInterceptor = logoutInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/mypage/**")
                .addPathPatterns("/logout")
                .addPathPatterns("/articles/**");

        registry.addInterceptor(logoutInterceptor)
                .addPathPatterns("/login")
                .addPathPatterns("/signup")
                .addPathPatterns("/users");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginUserHandlerMethodArgumentResolver());
    }
}
