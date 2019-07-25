package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.LoginInterceptor;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/write")
                .addPathPatterns("/articles/new")
                .addPathPatterns("/articles/*/edit")
                .addPathPatterns("/mypage")
                .addPathPatterns("/mypage-edit")
                .addPathPatterns("/withdrawal")
                .addPathPatterns("/login")
                .addPathPatterns("/signup")
                .addPathPatterns("/users")
                .addPathPatterns("/logout");
    }
}
