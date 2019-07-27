package techcourse.myblog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.AuthInterceptor;
import techcourse.myblog.interceptor.NotLoginInterceptor;
import techcourse.myblog.interceptor.LoginInterceptor;
import techcourse.myblog.users.UserSessionArgumentResolver;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final NotLoginInterceptor notLoginInterceptor;
    private final AuthInterceptor authInterceptor;
    private final LoginInterceptor loginInterceptor;

    private final UserSessionArgumentResolver userSessionArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(notLoginInterceptor)
                .addPathPatterns("/users")
                .addPathPatterns("/users/**")
                .excludePathPatterns("/users/new")
                .excludePathPatterns("/users/login");

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/users/login")
                .addPathPatterns("/users/new");

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/users/{id}")
                .addPathPatterns("/users/{id}/edit")
                .excludePathPatterns("/users/logout");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userSessionArgumentResolver);
    }
}
