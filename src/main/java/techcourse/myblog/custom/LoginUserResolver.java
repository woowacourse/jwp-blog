package techcourse.myblog.custom;

import techcourse.myblog.domain.user.Information;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.exception.UnauthorizedException;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoginUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		Object userEmail = webRequest.getAttribute("email", RequestAttributes.SCOPE_SESSION);

		if (userEmail != null) {
			Information information = new Information((String) userEmail);
			return new User(information);
		}
		throw new UnauthorizedException();
	}
}
