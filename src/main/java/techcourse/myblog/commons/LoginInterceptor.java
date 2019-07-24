package techcourse.myblog.commons;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String path = request.getRequestURI();
        String method = request.getMethod();
        boolean isLogin = session.getAttribute("user") != null;

        if (isLogin) {
            if (isLoginForm(path) || isSignup(path, method) || isSignupForm(path)) {
                response.sendRedirect("/");
                return false;
            }
            return true;
        }
        if (!isLoginForm(path)) {
            response.sendRedirect("/users/login");
            return false;
        }
        return true;
    }

    private boolean isSignupForm(String path) {
        return path.equals("/users/new");
    }

    private boolean isSignup(String path, String method) {
        return path.equals("/users") && method.equals("POST");
    }

    private boolean isLoginForm(String path) {
        return path.equals("/users/login");
    }
}