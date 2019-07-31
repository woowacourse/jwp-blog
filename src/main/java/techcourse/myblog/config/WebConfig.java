package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new NoneAuthInterceptor())
                .addPathPatterns("/articles/**", "/writing")
                .excludePathPatterns("/articles/{articleId}")
                .addPathPatterns("/users/**")
                .excludePathPatterns("/users", "/users/signup")
                .addPathPatterns("/auth/logout");

        registry.addInterceptor(new NeedAuthInterceptor())
                .addPathPatterns("/auth/login", "/users/signup");
    }
}
