package techcourse.myblog.support.handler;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.domain.User;
import techcourse.myblog.support.argument.resolver.SessionInfo;

import javax.servlet.http.HttpServletRequest;

public class MyArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == SessionInfo.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setUser(user);

        return sessionInfo;
    }
}
