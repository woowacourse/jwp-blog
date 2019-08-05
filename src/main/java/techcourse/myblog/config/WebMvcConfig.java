package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.argumentresolver.UserArgumentResolver;
import techcourse.myblog.interceptor.NoSignInInterceptor;
import techcourse.myblog.interceptor.SignedInInterceptor;
import techcourse.myblog.interceptor.UserInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor())
                .addPathPatterns("/**")
        ;

        registry.addInterceptor(new NoSignInInterceptor())
                .addPathPatterns("/accounts/profile/edit")
                .addPathPatterns("/logout")
                .addPathPatterns("/accounts/user")
                .addPathPatterns("/articles/**")
                .addPathPatterns("/comment/**")
        ;

        registry.addInterceptor(new SignedInInterceptor())
                .addPathPatterns("/accounts/signup")
                .addPathPatterns("/accounts/user")
                .addPathPatterns("/login")
        ;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver());
    }
}
