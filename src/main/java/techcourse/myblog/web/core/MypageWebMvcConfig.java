package techcourse.myblog.web.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MypageWebMvcConfig implements WebMvcConfigurer {

    @Qualifier(value = "mypageInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Autowired
    public MypageWebMvcConfig(MypageInterceptor mypageInterceptor) {
        this.handlerInterceptor = mypageInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/mypage/**");
    }
}
