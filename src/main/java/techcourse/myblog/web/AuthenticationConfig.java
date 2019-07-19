package techcourse.myblog.web;

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
                .excludePathPatterns("/");
    }

}
