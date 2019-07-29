package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;
    private final LogoutInterceptor logoutInterceptor;

    @Autowired
    public WebMvcConfig(LoginInterceptor loginInterceptor, LogoutInterceptor logoutInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.logoutInterceptor = logoutInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/login")
                .addPathPatterns("/users/new");

        registry.addInterceptor(logoutInterceptor)
                .addPathPatterns("/users/**")
                .excludePathPatterns("/users/new")
                .addPathPatterns("/articles/**");
    }


}