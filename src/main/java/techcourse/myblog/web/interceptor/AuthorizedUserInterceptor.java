package techcourse.myblog.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class AuthorizedUserInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<User> userSession = Optional.ofNullable((User) request.getSession().getAttribute("user"));

        if (userSession.isPresent()) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
