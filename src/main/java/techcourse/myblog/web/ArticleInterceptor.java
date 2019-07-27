package techcourse.myblog.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Component
public class ArticleInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user = (User) request.getSession().getAttribute("user");
        String path = request.getRequestURI();
        /**
         * 로그인 없으면 안되는 것들(관계매핑 전)
         * 1. Article생성               //articles/writing //aritlces
         * 2. Article 수정              //articles/{articleId}/edit
         * 3. Article 삭제               //articles/{articleId}
         */


        if (Pattern.matches("\\/articles\\/[0-9]*", path) && !request.getMethod().equals("DELETE")) {
            return true;
        }

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;

    }
}
