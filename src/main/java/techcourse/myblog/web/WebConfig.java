package techcourse.myblog.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.web.argumentResolver.AccessUserArgumentResolver;
import techcourse.myblog.web.interceptor.AuthenticationInterceptor;
import techcourse.myblog.web.interceptor.UnAuthenticationInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/logout")
                .addPathPatterns("/mypage")
                .addPathPatterns("/mypage/mypage-edit")
                .addPathPatterns("/writing")
                .addPathPatterns("/articles")
                .addPathPatterns("/articles/*/edit")
                .addPathPatterns("/articles/*/comments")
                .addPathPatterns("/articles/*/comments/*");

        registry.addInterceptor(new UnAuthenticationInterceptor())
                .addPathPatterns("/signup")
                .addPathPatterns("/users")
                .addPathPatterns("/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AccessUserArgumentResolver());
    }
}
