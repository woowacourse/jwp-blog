package techcourse.myblog.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import techcourse.myblog.interceptor.UnauthenticatedInterceptor;

@Configuration
public class UnauthenticatedConfig implements WebMvcConfigurer {

    @Qualifier(value = "unauthenticatedInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Autowired
    public UnauthenticatedConfig(UnauthenticatedInterceptor unauthenticatedInterceptor) {
        this.handlerInterceptor = unauthenticatedInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/mypage/**")
                .addPathPatterns("/logout");
    }
}
