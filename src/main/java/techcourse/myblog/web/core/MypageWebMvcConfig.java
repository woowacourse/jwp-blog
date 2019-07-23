package techcourse.myblog.web.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MyPageWebMvcConfig implements WebMvcConfigurer {

    @Qualifier(value = "myPageInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Autowired
    public MyPageWebMvcConfig(MyPageInterceptor myPageInterceptor) {
        this.handlerInterceptor = myPageInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/mypage/**");
    }
}
