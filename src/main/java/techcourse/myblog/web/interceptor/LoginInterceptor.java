package techcourse.myblog.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<User> userSession = Optional.ofNullable((User) request.getSession().getAttribute("user"));

        if (userSession.isPresent()) {
            return true;
        }
        response.sendRedirect("/login");
        return false;
    }
}
