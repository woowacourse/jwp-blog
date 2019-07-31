package techcourse.myblog.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<Object> loginUser = Optional.ofNullable(request.getSession().getAttribute("user"));

        if (loginUser.isPresent()) {
            return true;
        }

        response.sendRedirect("/login");
        return false;
    }
}
