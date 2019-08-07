package techcourse.myblog.web;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpServletRequest;

import static techcourse.myblog.service.UserService.LOGGED_IN_USER_SESSION_KEY;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedInUser.class);
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return (User) request.getSession().getAttribute(LOGGED_IN_USER_SESSION_KEY);
    }
}
