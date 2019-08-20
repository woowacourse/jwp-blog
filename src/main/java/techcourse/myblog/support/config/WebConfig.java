package techcourse.myblog.support.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.argumentResolver.SessionUserArgumentResolver;
import techcourse.myblog.web.interceptor.AlreadyLoggedInInterceptor;
import techcourse.myblog.web.interceptor.AuthApiInterceptor;
import techcourse.myblog.web.interceptor.AuthInterceptor;

import java.util.List;

@EnableJpaAuditing
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final AuthApiInterceptor authApiInterceptor;
    private final AlreadyLoggedInInterceptor alreadyLoggedInInterceptor;
    private final SessionUserArgumentResolver sessionUserArgumentResolver;

    public WebConfig(AuthInterceptor authInterceptor,
                     AuthApiInterceptor authApiInterceptor,
                     AlreadyLoggedInInterceptor alreadyLoggedInInterceptor,
                     SessionUserArgumentResolver sessionUserArgumentResolver) {
        this.authInterceptor = authInterceptor;
        this.authApiInterceptor = authApiInterceptor;
        this.alreadyLoggedInInterceptor = alreadyLoggedInInterceptor;
        this.sessionUserArgumentResolver = sessionUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/users")
                .addPathPatterns("/mypage-edit")
                .addPathPatterns("/mypage")
                .addPathPatterns("/mypage/*")
                .addPathPatterns("/articles");

        registry.addInterceptor(authApiInterceptor)
                .addPathPatterns("/api/comments")
                .addPathPatterns("/api/comments/*");

        registry.addInterceptor(alreadyLoggedInInterceptor)
                .addPathPatterns("/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(sessionUserArgumentResolver);
    }
}
