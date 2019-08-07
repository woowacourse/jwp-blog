package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.argumentResolver.LoginUserArgumentResolver;
import techcourse.myblog.web.interceptor.LoginInterceptor;
import techcourse.myblog.web.interceptor.UserAuthInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;
    private final UserAuthInterceptor userAuthInterceptor;

    @Autowired
    public WebMvcConfig(LoginInterceptor loginInterceptor, UserAuthInterceptor userAuthInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.userAuthInterceptor = userAuthInterceptor;
    }

    @Bean
    public LoginUserArgumentResolver loginUserMethodArgumentResolver() {
        return new LoginUserArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login")
                .excludePathPatterns("/signup")
                .excludePathPatterns("/users")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/articles/{articleId}")
                .excludePathPatterns("/articles/{articleId}/comments");

        registry.addInterceptor(userAuthInterceptor)
                .addPathPatterns("/user/update/{pageId}")
                .addPathPatterns("/user/delete/{pageId}");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserMethodArgumentResolver());
    }
}
