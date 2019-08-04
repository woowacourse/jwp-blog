package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.presentation.interceptor.CommonInterceptor;
import techcourse.myblog.presentation.interceptor.LogoutInterceptor;
import techcourse.myblog.presentation.support.LoginUserHandlerMethodArgumentResolver;
import techcourse.myblog.presentation.interceptor.LoginInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final HandlerInterceptor commonInterceptor;
    private final HandlerInterceptor loginInterceptor;
    private final HandlerInterceptor logoutInterceptor;

    public WebMvcConfig(final CommonInterceptor commonInterceptor,
                        final LoginInterceptor loginInterceptor,
                        final LogoutInterceptor logoutInterceptor) {
        this.commonInterceptor = commonInterceptor;
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

        registry.addInterceptor(commonInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/logout");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginUserHandlerMethodArgumentResolver());
    }
}
