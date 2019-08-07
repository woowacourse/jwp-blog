package techcourse.myblog.controller.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.annotation.UserFromSession;
import techcourse.myblog.annotation.UserFromSessionThatCanBeNull;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.UnauthenticatedUserException;

import javax.servlet.http.HttpServletRequest;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.hasParameterAnnotation(UserFromSession.class) ||
                parameter.hasParameterAnnotation(UserFromSessionThatCanBeNull.class);
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        User user = (User) request.getSession().getAttribute("user");

        if (parameter.hasParameterAnnotation(UserFromSession.class) && user == null) {
            throw new UnauthenticatedUserException();
        }

        return user;
    }
}
