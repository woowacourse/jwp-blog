package techcourse.myblog.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final ArticleInterceptor articleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(articleInterceptor)
            .addPathPatterns("/writing")
            .addPathPatterns("/articles/**")
            .excludePathPatterns("/mypage/**")
            .excludePathPatterns("/users/**")
            .excludePathPatterns("/signup")
            .excludePathPatterns("/login")
            .excludePathPatterns("/logout")
            .excludePathPatterns("/");
    }
}
