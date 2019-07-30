package techcourse.myblog.commons;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.users.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserDto.Response userDto = (UserDto.Response) session.getAttribute("user");
        String path = request.getRequestURI();

        if(!ObjectUtils.isEmpty(userDto) && !path.contains(String.valueOf(userDto.getId()))){
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}