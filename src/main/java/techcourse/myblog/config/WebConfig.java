package techcourse.myblog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.commons.AuthInterceptor;
import techcourse.myblog.commons.LoginInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/users/*")
                .excludePathPatterns("/users/new");

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/users/{id}")
                .addPathPatterns("/users/{id}/edit")
                .excludePathPatterns("/users/logout");
    }
}
