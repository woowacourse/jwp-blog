package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.NeedAuthInterceptor;
import techcourse.myblog.web.NoneAuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new NoneAuthInterceptor())
                .addPathPatterns("/articles/**", "/writing")
                .excludePathPatterns("/articles/{articleId}")
                .addPathPatterns("/auth/users/**")
                .excludePathPatterns("/auth/users/");

        registry.addInterceptor(new NeedAuthInterceptor())
                .addPathPatterns("/auth/login", "/auth/signup");
    }
}
