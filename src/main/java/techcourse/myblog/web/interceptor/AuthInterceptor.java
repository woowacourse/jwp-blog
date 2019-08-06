package techcourse.myblog.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.application.dto.UserResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserResponse userSession = (UserResponse) request.getSession().getAttribute("user");

        if (userSession == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

}
