package techcourse.myblog.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/accounts/profile/edit")
                .addPathPatterns("/writing")
                .addPathPatterns("/articles")
                .addPathPatterns("/articles/*/edit")
                .addPathPatterns("/accounts/delete")
                .addPathPatterns("/articles/*/comments")
                .addPathPatterns("/logout");

        registry.addInterceptor(new NonAuthInterceptor())
                .addPathPatterns("/accounts/signup")
                .addPathPatterns("/login");
    }
}

