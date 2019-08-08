package techcourse.myblog.web.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.exception.InvalidUserSessionException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class SessionUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == SessionUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();

        User user = (User) Optional.ofNullable(req.getSession().getAttribute("user"))
                .orElseThrow(InvalidUserSessionException::new);

        return new SessionUser(user);
    }
}
