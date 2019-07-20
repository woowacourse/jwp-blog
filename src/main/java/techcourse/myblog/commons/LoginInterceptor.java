package techcourse.myblog.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static techcourse.myblog.users.UserController.USER_BASE_URI;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String path = request.getRequestURI();
        String method = request.getMethod();
        boolean isLogin = session.getAttribute("user") != null;

        log.debug("PATH : {} METHOD : {} LOGIN : {} ", path, method, isLogin);

        if (isLogin) {
            return isNotException(response, path, method);
        }

        if (!(isLoginForm(path) || isSignup(path, method) || isSignupForm(path))) {
            response.sendRedirect(USER_BASE_URI + "/login");
            return false;
        }
        return true;
    }

    private boolean isNotException(final HttpServletResponse response, final String path, final String method) throws IOException {
        if (isLoginForm(path) || isSignup(path, method) || isSignupForm(path)) {
            response.sendRedirect("/");
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