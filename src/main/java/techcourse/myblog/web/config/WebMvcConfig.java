package techcourse.myblog.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.argument.LoginUserArgumentResolver;
import techcourse.myblog.web.interceptor.loggedIn.LoggedInInterceptor;
import techcourse.myblog.web.interceptor.login.LoginInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;
    private final LoggedInInterceptor loggedInInterceptor;

    public WebMvcConfig(LoginInterceptor loginInterceptor, LoggedInInterceptor loggedInInterceptor) {
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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}