package techcourse.myblog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    public static final String LOGIN_SESSION_KEY = "loginUser";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String requestUri= request.getRequestURI();
        String requestMethod = request.getMethod();
        boolean login = session.getAttribute(LOGIN_SESSION_KEY) != null;

        if (login) {
            if (isNeedLogin(requestUri, requestMethod)) {
                log.debug("login error!");
                response.sendRedirect("/");
                return false;
            }
            return true;
        }

        if (isNeedLogout(requestUri, requestMethod)) {
            log.debug("logout error!");
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    private boolean isNeedLogin(String requestUri, String requestMethod) {
        return requestUri.contains("/login")
                || requestUri.contains("/signup")
                || (requestUri.contains("/users") && requestMethod.equals("POST"));
    }

    private boolean isNeedLogout(String requestUri, String requestMethod) {
        return !requestUri.contains("/login")
                && (!requestUri.contains("/users") && !requestMethod.equals("POST"))
                && !requestUri.contains("/signup");
    }
}
