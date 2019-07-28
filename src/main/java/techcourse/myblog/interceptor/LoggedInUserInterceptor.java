package techcourse.myblog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static techcourse.myblog.web.UserController.LOGGED_IN_USER;

@Component
public class LoggedInUserInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        HttpSession session = request.getSession();
        String path = request.getRequestURI();

        if (isLoggedIn(session) && path.equals("/login")) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(LOGGED_IN_USER) != null;
    }
}
