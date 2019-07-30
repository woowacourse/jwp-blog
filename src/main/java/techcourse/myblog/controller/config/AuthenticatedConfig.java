package techcourse.myblog.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import techcourse.myblog.interceptor.AuthenticatedInterceptor;

@Configuration
public class AuthenticatedConfig implements WebMvcConfigurer {

    @Qualifier(value = "authenticatedInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Autowired
    public AuthenticatedConfig(AuthenticatedInterceptor authenticatedInterceptor) {
        this.handlerInterceptor = authenticatedInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/signup")
                .addPathPatterns("/login");
    }
}
