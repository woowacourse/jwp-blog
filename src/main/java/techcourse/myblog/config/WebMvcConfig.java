package techcourse.myblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.interceptor.BasicAuthInterceptor;
import techcourse.myblog.interceptor.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public BasicAuthInterceptor basicAuthInterceptor() {
        return new BasicAuthInterceptor();
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthInterceptor());

        // TODO : 경로 패턴을 상수화? 해야할 듯 (공통된 곳에서 부터 가르키도록 해야지... 컨트롤러랑 여기 둘 중 한 군데서 바꿔도 실수하지 않게)
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/users/{id}/mypage-edit");
    }
}
