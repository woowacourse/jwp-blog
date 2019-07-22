package techcourse.myblog.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final HandlerInterceptorAdapter interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns = Arrays.asList("/", "/users", "/login", "/signup", "/js/**", "/css/**", "/images/**");
        registry.addInterceptor(interceptor)
                .excludePathPatterns(patterns)
                .addPathPatterns("/**");
    }
}
