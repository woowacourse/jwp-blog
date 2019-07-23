package techcourse.myblog.web;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static techcourse.myblog.domain.User.UserService.LOGGED_IN_USER_SESSION_KEY;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute(LOGGED_IN_USER_SESSION_KEY) == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
