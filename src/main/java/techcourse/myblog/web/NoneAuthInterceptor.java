package techcourse.myblog.web;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NoneAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest req, final HttpServletResponse res, final Object handler) throws Exception {
        HttpSession session = req.getSession();

        if (session.getAttribute("username") == null) {
            res.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
