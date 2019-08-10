package techcourse.myblog.controller.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.controller.resolver.exception.NotLoggedInException;
import techcourse.myblog.service.dto.LoginUserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String LOGGED_IN_USER = "loggedInUser";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return LoginUserDto.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public LoginUserDto resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession httpSession = httpRequest.getSession();
        LoginUserDto user = (LoginUserDto) httpSession.getAttribute(LOGGED_IN_USER);
        if (user == null) {
            throw new NotLoggedInException();
        }
        return user;
    }
}
