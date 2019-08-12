package techcourse.myblog.support.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.support.argumentresolver.UserSessionArgumentResolver;
import techcourse.myblog.web.interceptor.AuthInterceptor;

import java.util.List;

@Configuration
@EnableJpaAuditing
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserSessionArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/users")
                .addPathPatterns("/mypage-edit")
                .addPathPatterns("/mypage")
                .addPathPatterns("/mypage/*")
                .addPathPatterns("/articles");
    }
}
