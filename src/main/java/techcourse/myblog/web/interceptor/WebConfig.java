package techcourse.myblog.web.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**")
            .excludePathPatterns("/images/**")
            .excludePathPatterns("/js/**")
            .excludePathPatterns("/")
            .excludePathPatterns("/articles/*")
            .excludePathPatterns("/users/login")
            .excludePathPatterns("/login")
            .excludePathPatterns("/users")
            .excludePathPatterns("/signup");

        registry.addInterceptor(new CommonModelInterceptor())
            .addPathPatterns("/**");
    }
}
