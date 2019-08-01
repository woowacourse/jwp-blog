package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.service.dto.user.UserResponse;
import techcourse.myblog.service.user.UserService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@Controller
@RequestMapping("/mypage")
public class UserPageController {
    final private UserService userService;

    @Autowired
    public UserPageController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ModelAndView showMyPage(final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mypage");
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @DeleteMapping("")
    public String deleteUser(final HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        userService.delete(user);
        return "redirect:/logout";
    }

    @GetMapping("/mypage-edit")
    public ModelAndView showMyPageEdit(final HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("mypage-edit");
        return modelAndView;
    }

    @PutMapping("/mypage-edit")
    public String editMyPage(final HttpSession session, final String name) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        session.setAttribute(USER_SESSION_KEY, userService.update(user.getEmail(), name));
        return "redirect:/mypage";
    }
}
