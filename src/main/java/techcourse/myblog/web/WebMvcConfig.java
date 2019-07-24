package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthLoginInterceptor authLoginInterceptor;

    @Autowired
    public WebMvcConfig(AuthLoginInterceptor authLoginInterceptor) {
        this.authLoginInterceptor = authLoginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authLoginInterceptor)
                .addPathPatterns("/**");
    }
}
