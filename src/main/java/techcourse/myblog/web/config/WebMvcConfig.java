package techcourse.myblog.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.interceptor.AuthorizedUserInterceptor;
import techcourse.myblog.web.interceptor.UnauthorizedUserInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UnauthorizedUserInterceptor unauthorizedUserInterceptor;
    private final AuthorizedUserInterceptor authorizedUserInterceptor;

    public WebMvcConfig(UnauthorizedUserInterceptor unauthorizedUserInterceptor,
                        AuthorizedUserInterceptor authorizedUserInterceptor) {
        this.unauthorizedUserInterceptor = unauthorizedUserInterceptor;
        this.authorizedUserInterceptor = authorizedUserInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(unauthorizedUserInterceptor)
                .addPathPatterns("/mypage/**")
                .addPathPatterns("/users/**");

        registry.addInterceptor(authorizedUserInterceptor)
                .addPathPatterns("/signup")
                .addPathPatterns("/login");
    }
}
