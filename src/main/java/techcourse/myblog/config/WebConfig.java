package techcourse.myblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer interceptorConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new AuthInterceptor())
                        .addPathPatterns("/profile")
                        .addPathPatterns("/profile/edit")
                        .addPathPatterns("/writing")
                        .addPathPatterns("/articles")
                        .addPathPatterns("/articles/*/edit")
                        .addPathPatterns("/articles/*/comment")
                        .addPathPatterns("/articles/*/comment/*");
            }
        };
    }
}