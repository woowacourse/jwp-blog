package techcourse.myblog.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.UserController;

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
        Object userDto = session.getAttribute(UserController.LOGIN_SESSION);

        if (modelAndView != null) {
            modelAndView.getModel().remove("userInfo");
        }

        if (userDto != null && modelAndView != null) {
            UserDto findUserDto = userService.findByUserEmail((UserDto) userDto);
            modelAndView.getModel().put("userInfo", findUserDto);
        }
    }
}
