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
