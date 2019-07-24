package techcourse.myblog.controller.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import techcourse.myblog.interceptor.AuthInterceptor;

@Configuration
public class AuthWebMvcConfig implements WebMvcConfigurer {

    @Qualifier(value = "authInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Autowired
    public AuthWebMvcConfig(AuthInterceptor authInterceptor) {
        this.handlerInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/signup")
                .addPathPatterns("/login");
    }
}
