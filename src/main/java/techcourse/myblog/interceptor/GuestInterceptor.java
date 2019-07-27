package techcourse.myblog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.service.exception.AccessNotPermittedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Map;

import static techcourse.myblog.service.exception.AccessNotPermittedException.PERMISSION_FAIL_MESSAGE;
import static techcourse.myblog.web.UserController.LOGGED_IN_USER;

@Component
public class GuestInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        HttpSession session = request.getSession();

        if (isLoggedOut(session) && !isGetArticles(request)) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    private boolean isGetArticles(HttpServletRequest request) {
        return request.getRequestURI().equals("/articles") && request.getMethod().equals("GET");
    }

    private boolean isLoggedOut(HttpSession session) {
        return session.getAttribute(LOGGED_IN_USER) == null;
    }
}
