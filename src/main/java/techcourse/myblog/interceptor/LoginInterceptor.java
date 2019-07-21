package techcourse.myblog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.domain.User.UserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getSession().getAttribute("userName") == null) {
            return sendRedirect(response);
        }
        return true;
    }

    private boolean sendRedirect(HttpServletResponse response) {
        try {
            response.sendRedirect("/login");
            return false;
        } catch (IOException e) {
            throw new UserException();
        }
    }
}
