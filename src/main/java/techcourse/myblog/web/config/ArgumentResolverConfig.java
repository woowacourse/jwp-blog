package techcourse.myblog.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.support.SessionUserArgumentResolver;

import java.util.List;

@Configuration
public class ArgumentResolverConfig implements WebMvcConfigurer {
    @Bean
    public SessionUserArgumentResolver sessionUserMethodArgumentResolver() {
        SessionUserArgumentResolver bean = new SessionUserArgumentResolver();
        return bean;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(sessionUserMethodArgumentResolver());
    }
}
