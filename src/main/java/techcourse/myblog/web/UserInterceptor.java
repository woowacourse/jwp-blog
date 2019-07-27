package techcourse.myblog.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 로그인 없으면 불가능한 것들
         * 1. 내정보 보기    /users/mypage
         * 2. 내정보 수정    /users/mypage/edit
         * 3. 회원 탈퇴     /users/mypage
         * 4. 회원들 정보 보기 /users
         *
         * 회원가입(?) -> 일단 exclude함
         *
         */

        User user = (User) request.getSession().getAttribute("user");
        //TODO if) path를 넣는다면 : path에 new만 있어도 되지 않을까..?
        //TODO 회원가입은 무조건 되게 한다면 path필요 없지 않나..?
        // 왜 reponse.setStatus해도 400이 안되는가..!!!!!!!!!
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

}
