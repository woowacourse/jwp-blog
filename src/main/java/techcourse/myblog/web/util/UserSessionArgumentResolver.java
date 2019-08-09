package techcourse.myblog.web.util;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class UserSessionArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String LOGGED_IN_USER = "loggedInUser";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserSessionDto.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = servletRequest.getSession();
        Optional<UserSessionDto> userSessionDto
                = Optional.ofNullable((UserSessionDto) session.getAttribute(LOGGED_IN_USER));

        return userSessionDto.orElseThrow(NotLoggedInException::new);
    }
}
