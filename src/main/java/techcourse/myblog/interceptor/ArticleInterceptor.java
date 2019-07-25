package techcourse.myblog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class ArticleInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<UserDto.Response> user = Optional.ofNullable((UserDto.Response) request.getSession().getAttribute("user"));

        if (request.getMethod().equals("GET") && request.getRequestURI().matches("(\\/articles\\/)([0-9]+)$")){
            return true;
        }

        if (!user.isPresent()) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}

