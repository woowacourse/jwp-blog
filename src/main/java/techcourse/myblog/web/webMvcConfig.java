package techcourse.myblog.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.converter.ToArticle;
import techcourse.myblog.converter.ToUser;
import techcourse.myblog.interceptor.loggedIn.LoggedInInterceptor;
import techcourse.myblog.interceptor.login.LoginInterceptor;

@Configuration
public class webMvcConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;
    private final LoggedInInterceptor loggedInInterceptor;

    public webMvcConfig(LoginInterceptor loginInterceptor, LoggedInInterceptor loggedInInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.loggedInInterceptor = loggedInInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/users")
                .excludePathPatterns("/")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/signup")
                .excludePathPatterns("/images/**");

        registry.addInterceptor(loggedInInterceptor)
                .addPathPatterns("/login")
                .addPathPatterns("/signup");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ToArticle());
        registry.addConverter(new ToUser());
    }
}