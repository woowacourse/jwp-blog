package techcourse.myblog.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.application.dto.UserResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 회원 가입 통과
        if ("/users".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            return true;
        }

        UserResponse userSession = (UserResponse) request.getSession().getAttribute("user");

        // 로그인 안할 때 통과
        if ("/login".equals(request.getRequestURI()) && userSession == null) {
            return true;
        }

        // 이미 로그인 했다면 통과하지 못함
        if ("/login".equals(request.getRequestURI()) && userSession != null) {
            response.sendRedirect("/");
            return false;
        }

        if (userSession == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
