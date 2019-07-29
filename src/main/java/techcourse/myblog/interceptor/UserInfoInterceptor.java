package techcourse.myblog.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class UserInfoInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");

        if (modelAndView != null) {
            modelAndView.getModel().remove("userInfo");
        }

        if (userId != null && modelAndView != null) {
            UserDto userDto = userService.readWithoutPasswordById((long) userId);

            modelAndView.getModel().put("userInfo", userDto);
        }
    }
}
