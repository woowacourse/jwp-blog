package techcourse.myblog.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Component
public class AlreadyLoggedInInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(AlreadyLoggedInInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("uri: {}", request.getRequestURI());

        HttpSession session = request.getSession(false);
        if (Objects.nonNull(session) && Objects.nonNull(session.getAttribute("user"))) {
            log.debug("already logged in..!!");
            response.sendRedirect("/");

            return false;
        }

        return true;
    }
}
