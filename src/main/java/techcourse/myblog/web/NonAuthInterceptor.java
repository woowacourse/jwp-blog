package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NonAuthInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(NonAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("called");

        if (request.getSession().getAttribute("user") != null) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
