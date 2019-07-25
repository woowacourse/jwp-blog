package techcourse.myblog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AlreadyLoggedInInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(AlreadyLoggedInInterceptor.class);

    @Autowired
    private LoginService loginService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("uri: {}", request.getRequestURI());

        if (loginService.isLoggedIn(request.getSession(false))) {
            log.debug("already logged in..!");
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
