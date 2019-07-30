package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.GuestInterceptor;
import techcourse.myblog.interceptor.LoggedInUserInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final GuestInterceptor guestInterceptor;
    private final LoggedInUserInterceptor loggedInUserInterceptor;

    public WebConfig(GuestInterceptor guestInterceptor, LoggedInUserInterceptor loggedInUserInterceptor) {
        this.guestInterceptor = guestInterceptor;
        this.loggedInUserInterceptor = loggedInUserInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(guestInterceptor)
                .addPathPatterns("/users/**", "/comments/**", "/articles/**")
                .excludePathPatterns("/users/sign-up");

        registry.addInterceptor(loggedInUserInterceptor)
                .addPathPatterns("/login", "/users/sign-up");
    }
}