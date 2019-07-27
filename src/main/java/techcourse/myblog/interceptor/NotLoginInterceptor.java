package techcourse.myblog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static techcourse.myblog.users.UserSession.USER_SESSION;

@Component
public class NotLoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(NotLoginInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        HttpSession session = request.getSession();
        String path = request.getRequestURI();
        String method = request.getMethod();
        log.debug("PATH : {} METHOD : {} ", path, method);

        if (session.getAttribute(USER_SESSION) == null && !isException(response, path, method)) {
            response.sendRedirect("/users/login");
            return false;
        }
        return true;
    }

    private boolean isException(final HttpServletResponse response, final String path, final String method) throws IOException {
        return isSignup(path, method);
    }


    private boolean isSignup(String path, String method) {
        return path.equals("/users") && method.equals("POST");
    }

}