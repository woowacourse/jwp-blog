package techcourse.myblog.support.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.controller.interceptor.CertificationInterceptor;

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
}
