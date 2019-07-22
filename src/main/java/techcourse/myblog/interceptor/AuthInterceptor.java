package techcourse.myblog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.users.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static techcourse.myblog.users.UserController.USER_SESSION;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute(USER_SESSION);
        String path = request.getRequestURI();

        if (!ObjectUtils.isEmpty(userSession) && !path.contains(String.valueOf(userSession.getId()))) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}