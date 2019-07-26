package techcourse.myblog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.domain.user.UserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getSession().getAttribute("userName") == null) {
            log.debug("Session.userName is null {}", request.getSession());
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
