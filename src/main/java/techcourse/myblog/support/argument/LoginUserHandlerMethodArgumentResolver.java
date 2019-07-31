package techcourse.myblog.support.argument;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpServletRequest;

public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == LoginUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();

        LoginUser loginUser = new LoginUser();
        User user = (User) req.getSession().getAttribute("user");
        loginUser.setUser(user);

        return loginUser;
    }
}
