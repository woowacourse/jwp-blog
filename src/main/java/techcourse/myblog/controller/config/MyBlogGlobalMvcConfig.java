package techcourse.myblog.controller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import techcourse.myblog.controller.argumentresolver.RedirectArgumentResolver;
import techcourse.myblog.controller.argumentresolver.SessionArgumentResolver;

@Configuration
public class MyBlogGlobalMvcConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SessionArgumentResolver());
        resolvers.add(new RedirectArgumentResolver());
    }
}
