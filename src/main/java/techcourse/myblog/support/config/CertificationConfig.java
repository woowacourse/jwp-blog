package techcourse.myblog.support.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.controller.interceptor.CertificationInterceptor;
import techcourse.myblog.support.handler.MyArgumentResolver;

import java.util.List;

@Configuration
public class CertificationConfig implements WebMvcConfigurer {
    private final CertificationInterceptor certificationInterceptor;

    public CertificationConfig(CertificationInterceptor certificationInterceptor) {
        this.certificationInterceptor = certificationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(certificationInterceptor)
                .addPathPatterns("/mypage/**")
                .addPathPatterns("/logout");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MyArgumentResolver());
    }
}
