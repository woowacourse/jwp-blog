package techcourse.myblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.interceptor.LoggingInterceptor;
import techcourse.myblog.web.interceptor.UserInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final UserInterceptor userInterceptor;

    @Autowired
    public WebMvcConfig(LoggingInterceptor loggingInterceptor, UserInterceptor userInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
        this.userInterceptor = userInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
            .excludePathPatterns("/h2-console")
            .excludePathPatterns("/css/**")
            .excludePathPatterns("/js/**")
            .excludePathPatterns("/images/**");

        registry.addInterceptor(userInterceptor)
            .excludePathPatterns("/h2-console")
            .excludePathPatterns("/css/**")
            .excludePathPatterns("/js/**")
            .excludePathPatterns("/images/**");
    }
}
