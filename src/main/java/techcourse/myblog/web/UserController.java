package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.service.dto.user.UserRequest;
import techcourse.myblog.service.dto.user.UserResponse;
import techcourse.myblog.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@Controller
public class UserController {
    final private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignUp() {
        return "signup";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<UserResponse> userResponses = userService.findAll();
        model.addAttribute("users", userResponses);
        return "user-list";
    }

    @PostMapping("/users")
    public String registerUsers(@Valid final UserRequest userRequest, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.save(userRequest);
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public ModelAndView showMyPage(final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mypage");
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @DeleteMapping("/mypage")
    public String deleteUser(final HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        userService.delete(user);
        return "redirect:/logout";
    }

    @GetMapping("/logout")
    public String logOut(final HttpServletRequest request) {
        request.getSession().removeAttribute(USER_SESSION_KEY);
        return "redirect:/";
    }

    @GetMapping("/mypage/mypage-edit")
    public ModelAndView showMyPageEdit(final HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("mypage-edit");
        return modelAndView;
    }

    @PutMapping("/mypage/mypage-edit")
    public String editMyPage(final HttpSession session, final String name) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        session.setAttribute(USER_SESSION_KEY, userService.update(user.getEmail(), name));
        return "redirect:/mypage";
    }
}
