package techcourse.myblog.support.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techcourse.myblog.support.encrytor.EncryptHelper;
import techcourse.myblog.support.encrytor.PasswordBCryptor;
import techcourse.myblog.web.interceptor.AuthInterceptor;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer interceptorConfigure() {
        //TODO 댓글, 게시글 CRUD 시 로그인된 상태 확인 후 로그인하지 않은 상태이면 로그인 화면으로 이동
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new AuthInterceptor())
                        .addPathPatterns("/users")
                        .addPathPatterns("/login")
                        .addPathPatterns("/mypage")
                        .addPathPatterns("/mypage/*")
                        .addPathPatterns("/mypage-edit")
                        .addPathPatterns("/articles/*")
                        .addPathPatterns("/articles/*/edit")
                        .addPathPatterns("/articles/*/comments")
                        .addPathPatterns("/articles/*/comments/*")
                ;
            }
        };
    }

    @Bean
    public EncryptHelper encryptConfigure() {
        return new PasswordBCryptor();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
