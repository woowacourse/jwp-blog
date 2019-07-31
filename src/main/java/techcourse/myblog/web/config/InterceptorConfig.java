package techcourse.myblog.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.interceptor.AuthInterceptor;

@Configuration
public class InterceptorConfig {
    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new AuthInterceptor())
                        .addPathPatterns("/articles", "/articles/*")
                        .addPathPatterns("/mypage", "/mypage/*")
                        .addPathPatterns("/comment", "/comment/*");
            }
        };
    }
}
