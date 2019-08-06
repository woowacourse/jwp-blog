package techcourse.myblog.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthLoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute("loginUser");
        String path = request.getRequestURI();

        if (loginUser == null && path.contains("users/new")) {
            return true;
        }

        if (loginUser == null && (path.contains("mypage") || path.contains("users"))) {
            response.sendRedirect("/login");
            return false;
        }

        if (loginUser != null && (path.contains("login") || path.contains("signup"))) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
