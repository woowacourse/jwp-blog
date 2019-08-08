package techcourse.myblog.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.loggedIn.LoggedInInterceptor;
import techcourse.myblog.interceptor.login.LoginInterceptor;
import techcourse.myblog.utils.custum.LoginUserHandlerMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;
    private final LoggedInInterceptor loggedInInterceptor;

    public WebMvcConfig(LoginInterceptor loginInterceptor, LoggedInInterceptor loggedInInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.loggedInInterceptor = loggedInInterceptor;
    }

    @Bean
    public LoginUserHandlerMethodArgumentResolver loginUserResolver() {
        return new LoginUserHandlerMethodArgumentResolver();
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
        resolvers.add(loginUserResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}