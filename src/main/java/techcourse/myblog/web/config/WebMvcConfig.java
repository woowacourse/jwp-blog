package techcourse.myblog.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.support.LoginUserHandlerMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    @Qualifier(value = "authenticationInterceptor")
    private HandlerInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**");
    }

    @Bean
    public LoginUserHandlerMethodArgumentResolver loginUserMethodArgumentResolver() {
        LoginUserHandlerMethodArgumentResolver bean = new LoginUserHandlerMethodArgumentResolver();
        return bean;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserMethodArgumentResolver());
    }
}
