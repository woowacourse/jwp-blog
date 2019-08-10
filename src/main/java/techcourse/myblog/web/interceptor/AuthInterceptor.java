package techcourse.myblog.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("begin");

        if (request.getRequestURI().equals("/users") && request.getMethod().equals("POST")) {
            return true;
        }

        User userSession = (User) request.getSession().getAttribute("user");

        log.debug("id: {}", userSession.getId());

        if (userSession == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
