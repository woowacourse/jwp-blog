package techcourse.myblog.web.interceptor.loggedIn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class LoggedInInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoggedInInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();
        log.info("uri : {} ", uri);

        if ("/login".equals(uri) || "/signup".equals(uri)) {
            if (!SessionUtil.isNull(session)) {
                response.sendRedirect("redirect:/");
                return false;
            }
        }
        return true;
    }
}
