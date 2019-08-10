package techcourse.myblog.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.interceptor.LogoutInterceptor;

@Configuration
public class LogoutInterceptorConfig implements WebMvcConfigurer {
    private final HandlerInterceptor logoutInterceptor;

    public LogoutInterceptorConfig(final LogoutInterceptor logoutInterceptor) {
        this.logoutInterceptor = logoutInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logoutInterceptor)
                .addPathPatterns("/login")
                .addPathPatterns("/signup")
                .addPathPatterns("/users");
    }
}
