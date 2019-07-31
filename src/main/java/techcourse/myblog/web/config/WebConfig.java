package techcourse.myblog.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.controller.CustomArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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

    @Bean
    public WebMvcConfigurer argumentResolverConfigure(){
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new CustomArgumentResolver());
            }
        };
    }

}