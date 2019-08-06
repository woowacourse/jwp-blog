package techcourse.myblog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class GuestInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        HttpSession session = request.getSession();

        if (isLoggedOut(session) && !isSave(request)) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    private boolean isSave(HttpServletRequest request) {
        return request.getRequestURI().equals("/users")
                && request.getMethod().equals("POST");
    }

    private boolean isLoggedOut(HttpSession session) {
        return session.getAttribute("user") == null;
    }
}