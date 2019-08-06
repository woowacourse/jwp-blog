package techcourse.myblog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class UserInterceptor extends HandlerInterceptorAdapter {
    //TODO : argumentResolver 유저가 필요한 부분만.......
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) request.getSession().getAttribute("user"));

        //로그인
        if (request.getRequestURI().equals("/login")) {
            if (userSession.isPresent()) {
                response.sendRedirect("/");
                return false;
            }
            return true;
        }

        //회원가입 페이지, 요청
        if (request.getRequestURI().equals("/signup") ||
            (request.getMethod().equals("POST") && request.getRequestURI().equals("/users"))) {
            if (userSession.isPresent()) {
                response.sendRedirect("/");
                return false;
            }
            return true;
        }

        //로그아웃
        if (request.getRequestURI().equals("/logout") && !userSession.isPresent()) {
            if (!userSession.isPresent()) {
                response.sendRedirect("/");
                return false;
            }
            return true;
        }

        //유저 리스트, 마이페이지 조회
        if ((request.getMethod().equals("GET") && request.getRequestURI().equals("/users")) ||
            (request.getMethod().equals("GET") && request.getRequestURI().matches("(\\/mypage\\/)([0-9]+)$"))) {
            return true;
        }


        //회원수정, 탈퇴, 수정 페이지
        if (!userSession.isPresent()) {
            response.sendRedirect("/login");
            return false;
        } else {
            return true;
        }
    }
}
