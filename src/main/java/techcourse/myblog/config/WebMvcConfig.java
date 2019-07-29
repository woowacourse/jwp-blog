package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.LoginInterceptor;
import techcourse.myblog.interceptor.UserInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/accounts/profile/edit")
                .addPathPatterns("/logout")
                .addPathPatterns("/accounts/user")
                .addPathPatterns("/articles/**")
        ;

        registry.addInterceptor(new UserInterceptor())
                .addPathPatterns("/accounts/signup")
                .addPathPatterns("/accounts/user")
                .addPathPatterns("/login");
    }
}
