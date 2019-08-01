package techcourse.myblog.web.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/logout")
                .addPathPatterns("/mypage")
                .addPathPatterns("/mypage/mypage-edit")
                .addPathPatterns("/writing")
                .addPathPatterns("/articles")
                .addPathPatterns("/articles/*/edit")
                .addPathPatterns("/articles/*/comments")
                .addPathPatterns("/articles/*/comments/*");

        registry.addInterceptor(new UnAuthenticationInterceptor())
                .addPathPatterns("/signup")
                .addPathPatterns("/users")
                .addPathPatterns("/login");
    }
}
