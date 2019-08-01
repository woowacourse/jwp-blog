package techcourse.myblog.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NeedAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest req, final HttpServletResponse res, final Object handler) throws Exception {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") != null) {
            res.sendRedirect("/");
            return false;
        }
        return true;
    }

}