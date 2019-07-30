package techcourse.myblog.presentation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.presentation.interceptor.LoginCheckInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/mypage")
                .addPathPatterns("/mypage/edit")
                .addPathPatterns("/logout");
    }
}