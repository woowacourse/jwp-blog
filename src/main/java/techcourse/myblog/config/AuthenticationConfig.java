package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.AuthenticationInterceptor;

@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/users")
                .addPathPatterns("/users/mypage/edit")
                .addPathPatterns("/users/mypage")
                .addPathPatterns("/articles/new")
                .addPathPatterns("/articles/*/edit")
                .addPathPatterns("/login")
                .addPathPatterns("/users/signup")
                .excludePathPatterns("/");
    }

}
