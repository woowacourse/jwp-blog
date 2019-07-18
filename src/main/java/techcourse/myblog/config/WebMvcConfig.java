package techcourse.myblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.interceptor.AuthInterceptor;
import techcourse.myblog.web.interceptor.LoggingInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final AuthInterceptor authInterceptor;

    @Autowired
    public WebMvcConfig(LoggingInterceptor loggingInterceptor, AuthInterceptor authInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
            .excludePathPatterns("/h2-console")
            .excludePathPatterns("/css/**")
            .excludePathPatterns("/js/**")
            .excludePathPatterns("/images/**"); // Exclude h2 console path from interceptor
        registry.addInterceptor(authInterceptor)
            .excludePathPatterns("/h2-console")
            .excludePathPatterns("/css/**")
            .excludePathPatterns("/js/**")
            .excludePathPatterns("/images/**");
    }
}
