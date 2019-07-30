package techcourse.myblog.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final ArticleInterceptor articleInterceptor;
    private final UserInterceptor userInterceptor;
    private final CommentInterceptor commentInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(articleInterceptor)
            .addPathPatterns("/writing")
            .addPathPatterns("/articles/**");

        registry.addInterceptor(userInterceptor)
            .addPathPatterns("/signup")
            .addPathPatterns("/users/**")
            .addPathPatterns("/mypage/**")
            .addPathPatterns("/login")
            .addPathPatterns("/logout");

        registry.addInterceptor(commentInterceptor)
            .addPathPatterns("/comments/**");

    }
}
