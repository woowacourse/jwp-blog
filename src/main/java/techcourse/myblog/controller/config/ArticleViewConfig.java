package techcourse.myblog.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import techcourse.myblog.interceptor.ArticleViewInterceptor;

@Configuration
public class ArticleViewConfig implements WebMvcConfigurer {

    @Qualifier(value = "articleViewInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Autowired
    public ArticleViewConfig(ArticleViewInterceptor articleViewInterceptor) {
        this.handlerInterceptor = articleViewInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/articles/**")
                .addPathPatterns("/")
                .excludePathPatterns("/articles/writing");
    }
}
