package techcourse.myblog.presentation.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.domain.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class CommonInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Optional<User> loginUser = Optional.ofNullable((User) request.getSession().getAttribute("user"));

        loginUser.ifPresent(user -> modelAndView.addObject("user", user));
    }
}
