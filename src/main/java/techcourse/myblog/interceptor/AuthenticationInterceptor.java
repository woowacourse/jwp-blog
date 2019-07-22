package techcourse.myblog.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.web.LoginUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (request.getSession().getAttribute(LoginUtil.USER_SESSION_KEY) == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
