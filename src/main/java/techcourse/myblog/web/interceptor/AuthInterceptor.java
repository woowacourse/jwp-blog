package techcourse.myblog.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().equals("/users") && request.getMethod().equals("POST")) {
            return true;
        }

        Optional<User> userSession = Optional.ofNullable((User) request.getSession().getAttribute("user"));

        if (!userSession.isPresent()) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
