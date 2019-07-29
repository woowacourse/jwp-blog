package techcourse.myblog.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        if ("/login".equals(req.getRequestURI()) && req.getMethod().equals("POST")) {
            return true;
        }
        if(req.getSession().getAttribute("email") == null) {
            res.sendRedirect("/login");
            return false;
        }
        return true;
    }
}