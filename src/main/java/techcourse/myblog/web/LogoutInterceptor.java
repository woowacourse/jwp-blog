package techcourse.myblog.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Component
public class LogoutInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        String path = request.getRequestURI();

        if (Pattern.matches("\\/articles\\/[0-9]*", path) && request.getMethod().equals("GET")) {
            return true;
        }

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

}
