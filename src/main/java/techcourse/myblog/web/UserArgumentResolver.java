package techcourse.myblog.web;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpServletRequest;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedUser.class);
    }

    @Override
    public UserPublicInfoDto resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        UserPublicInfoDto userPublicInfoDto = (UserPublicInfoDto) request.getSession().getAttribute(UserController.LOGGED_IN_USER);

        if (userPublicInfoDto != null) {
            return userPublicInfoDto;
        }
        throw new NotLoggedInException();
    }
}
