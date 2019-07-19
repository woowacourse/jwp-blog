package techcourse.myblog.config;

import org.springframework.web.servlet.HandlerInterceptor;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if (request.getRequestURI().equals("/user/login") && request.getMethod().equals("POST")) {
                return true;
            }

            User userDtoSession = (User) request.getSession().getAttribute("user");

            if (userDtoSession == null) {
                response.sendRedirect("/user");
                return false;
            }
            return true;
        }
    }

