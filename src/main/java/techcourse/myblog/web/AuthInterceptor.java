package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static techcourse.myblog.service.UserService.LOGGED_IN_USER_SESSION_KEY;

public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("called");
        
        if (request.getSession().getAttribute(LOGGED_IN_USER_SESSION_KEY) == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
