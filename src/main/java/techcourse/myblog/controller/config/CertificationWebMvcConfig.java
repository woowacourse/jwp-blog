package techcourse.myblog.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import techcourse.myblog.interceptor.CertificationInterceptor;

@Configuration
public class CertificationWebMvcConfig implements WebMvcConfigurer {

    @Qualifier(value = "certificationInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Autowired
    public CertificationWebMvcConfig(CertificationInterceptor certificationInterceptor) {
        this.handlerInterceptor = certificationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/mypage/**")
                .addPathPatterns("/articles/**")
                .addPathPatterns("/logout");
    }
}
