package techcourse.myblog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static techcourse.myblog.util.SessionKeys.USER;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession();
        if (isAlreadyLogin(httpSession)) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    private boolean isAlreadyLogin(HttpSession httpSession) {
        return httpSession.getAttribute(USER) == null;
    }
}
