package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserEditRequest;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.dto.UserRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String USER = "user";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String createLoginForm(HttpServletRequest request, UserLoginRequest userLoginRequest) {
        log.debug("begin");

        if (request.getSession().getAttribute(USER) == null) {
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String createSignForm(UserRequest userRequest) {
        log.debug("begin");

        return "signup";
    }

    @PostMapping("/users")
    public String saveUser(@Valid UserRequest userRequest, BindingResult bindingResult) {
        log.debug("begin");

        if (bindingResult.hasErrors()) {
            return "signup";
        }
        User user = userService.saveUser(userRequest);
        log.info("user: {} ", user);

        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        log.debug("begin");

        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String myPageForm(Model model, HttpServletRequest request) {
        log.debug("begin");

        model.addAttribute(USER, request.getSession().getAttribute(USER));
        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String myPageEditForm(UserEditRequest userEditRequest) {
        return "mypage-edit";
    }

    @PostMapping("/login")
    public String login(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        log.debug("begin");

        User user = userService.findUserByEmail(userLoginRequest);
        request.getSession().setAttribute(USER, user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        log.debug("begin");

        request.getSession().removeAttribute(USER);
        return "redirect:/";
    }

    @PutMapping("/users/{userId}")
    public String editUser(@PathVariable("userId") Long userId, HttpServletRequest request, @Valid UserEditRequest userEditRequest, BindingResult bindingResult) {
        log.debug("begin");

        if (bindingResult.hasErrors()) {
            return "mypage-edit";
        }
        User user = userService.editUserName(userId, userEditRequest.getName());
        request.getSession().setAttribute(USER, user);
        return "redirect:/";
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId, HttpServletRequest request) {
        log.debug("begin");

        userService.deleteById(userId);
        log.info("userId: {} ", userId);

        request.getSession().removeAttribute(USER);
        return "redirect:/";
    }
}
