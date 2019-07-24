package techcourse.myblog.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LogoutInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().equals("/user/login") && request.getMethod().equals("POST")) {
            return true;
        }
        UserDto userDtoSession = (UserDto) request.getSession(false).getAttribute("user");

        if (userDtoSession == null) {
            response.sendRedirect("/user");
            return false;
        }
        return true;
    }
}

