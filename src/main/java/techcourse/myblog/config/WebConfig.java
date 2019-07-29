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
//                        .addPathPatterns("/articles/*")
//                        .addPathPatterns("/login")
//                        .addPathPatterns("/profile")
//                        .addPathPatterns("/profile/edit");
            }
        };
    }
}