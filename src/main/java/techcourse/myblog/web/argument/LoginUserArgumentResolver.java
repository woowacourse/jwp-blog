package techcourse.myblog.web.argument;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.comment.exception.AuthenticationException;
import techcourse.myblog.dto.UserResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static techcourse.myblog.utils.session.SessionContext.USER;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * resolveArgument를 실행 할 수 있는 method인지 판별
     *
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == UserResponseDto.class;
    }

    /**
     * Method parameter에 대한 Argument Resolver로직 처리
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession httpSession = request.getSession();
        UserResponseDto userResponseDto = Optional.ofNullable((UserResponseDto) httpSession.getAttribute(USER))
                .orElseThrow(AuthenticationException::new);

        return userResponseDto;
    }
}
