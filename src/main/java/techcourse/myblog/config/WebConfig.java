package techcourse.myblog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.commons.AuthInterceptor;
import techcourse.myblog.commons.LoginInterceptor;

import static techcourse.myblog.users.UserController.USER_BASE_URI;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(USER_BASE_URI + "/*");

        registry.addInterceptor(authInterceptor)
                .addPathPatterns(USER_BASE_URI + "/{id}")
                .addPathPatterns(USER_BASE_URI + "/{id}/edit")
                .excludePathPatterns(USER_BASE_URI + "/logout");
    }
}
