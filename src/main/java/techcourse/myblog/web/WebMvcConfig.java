package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final ArticleInterceptor articleInterceptor;
    private final UserInterceptor userInterceptor;

    @Autowired
    public WebMvcConfig(ArticleInterceptor articleInterceptor, UserInterceptor userInterceptor) {
        this.articleInterceptor = articleInterceptor;
        this.userInterceptor = userInterceptor;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authLoginInterceptor)
//                .addPathPatterns("/**");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/users/**")
                .excludePathPatterns("/users/signup")
                .excludePathPatterns("/users/new");


        registry.addInterceptor(articleInterceptor)
                .addPathPatterns("/articles/**");
    }


}