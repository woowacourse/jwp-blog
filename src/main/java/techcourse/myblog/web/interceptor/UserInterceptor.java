package techcourse.myblog.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("/accounts/user".equals(request.getRequestURI()) && "DELETE".equals(request.getMethod())) {
            return true;
        }

        if (request.getSession().getAttribute("user") != null) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
