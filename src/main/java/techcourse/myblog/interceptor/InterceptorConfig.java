package techcourse.myblog.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.loggedIn.LoggedInInterceptor;
import techcourse.myblog.interceptor.login.LoginInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;
    private final LoggedInInterceptor loggedInInterceptor;

    public InterceptorConfig(LoginInterceptor loginInterceptor, LoggedInInterceptor loggedInInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.loggedInInterceptor = loggedInInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/writing")
                .addPathPatterns("/articles")
                .addPathPatterns("/articles/**")
                .addPathPatterns("/users")
                .addPathPatterns("/mypage")
                .addPathPatterns("/mypage-edit");

        registry.addInterceptor(loggedInInterceptor)
                .addPathPatterns("/login")
                .addPathPatterns("/signup");
    }
}