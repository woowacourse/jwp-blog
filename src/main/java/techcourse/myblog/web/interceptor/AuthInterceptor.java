package techcourse.myblog.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    private final LoginService loginService;

    public AuthInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("begin");

        if (request.getRequestURI().equals("/users") && request.getMethod().equals("POST")) {
            return true;
        }

        if (request.getRequestURI().matches("/api/comments/*") && request.getMethod().equals("GET")) {
            return true;
        }

        Optional<User> userSession = loginService.retrieveLoggedInUser(request.getSession(false));
        if (canNotHandleFromOriginUri(userSession)) {
            response.sendRedirect("/login");

            return false;
        }

        log.debug("id: {}", userSession.get().getId());

        return true;
    }

    private boolean canNotHandleFromOriginUri(Optional<User> userSession) {
        return !userSession.isPresent();
    }
}
