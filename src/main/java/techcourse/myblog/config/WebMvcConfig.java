package techcourse.myblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.LoginUserInterceptor;
import techcourse.myblog.interceptor.UserInfoInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    @Qualifier(value = "loginInterceptor")
    private HandlerInterceptor interceptor;

    @Autowired
    @Qualifier(value = "loginUserInterceptor")
    private LoginUserInterceptor loginUserInterceptor;


    @Autowired
    @Qualifier(value = "userInfoInterceptor")
    private UserInfoInterceptor userInfoInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/users/**");

        registry.addInterceptor(userInfoInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(loginUserInterceptor)
                .addPathPatterns("/signup")
                .addPathPatterns("/login");
    }
}