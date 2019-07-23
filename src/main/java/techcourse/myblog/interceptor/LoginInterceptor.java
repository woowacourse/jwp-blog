package techcourse.myblog.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDto userDtoSession = (UserDto) request.getSession().getAttribute("user");

        if (userDtoSession == null) {
            return true;
        }
        response.sendRedirect("/");
        return false;
    }
}
