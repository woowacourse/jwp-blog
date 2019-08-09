package techcourse.myblog.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static techcourse.myblog.web.UserSession.USER_SESSION;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        HttpSession session = request.getSession();
        String path = request.getRequestURI();
        String method = request.getMethod();
        log.debug("PATH : {} METHOD : {} ", path, method);

        if (session.getAttribute(USER_SESSION) != null) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
