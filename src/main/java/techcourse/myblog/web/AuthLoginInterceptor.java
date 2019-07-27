package techcourse.myblog.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("user");
        String path = request.getRequestURI();

        //TODO 로그인X && 회원가입 하려함 -> 허락
        if (loginUser == null && path.contains("users/new")) {
            return true;
        }

        //TODO 로그인X && 내정보수정,회원리스트조회,내정보보기 -> 불허
        if (loginUser == null && (path.contains("mypage") || path.contains("users"))) {
            response.sendRedirect("/login");
            return false;
        }

        //TODO 로그인O && 로그인,회원가입 -> 불허
        if (loginUser != null && (path.contains("login") || path.contains("signup"))) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
