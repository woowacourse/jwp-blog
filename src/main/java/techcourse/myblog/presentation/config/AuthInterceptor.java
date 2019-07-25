package techcourse.myblog.presentation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("/user/login".equals(request.getRequestURI()) && request.getMethod().equals("POST")) {
            return true;
        }
        User userSession = (User) request.getSession().getAttribute("user");
        if (userSession == null) {
            response.sendRedirect("/user");
            return false;
        }
        return true;
    }
}

