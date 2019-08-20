package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserEditRequest;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.dto.UserRequest;
import techcourse.myblog.web.argumentResolver.SessionUser;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String USER = "user";

    private final UserService userService;
    private final LoginService loginService;

    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String createLoginForm(UserLoginRequest userLoginRequest) {
        log.debug("begin");

        return "login";
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
    public String myPageForm(Model model, @SessionUser User user) {
        log.debug("begin");

        model.addAttribute(USER, user);
        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String myPageEditForm(UserEditRequest userEditRequest) {
        return "mypage-edit";
    }

    @PostMapping("/login")
    public String login(UserLoginRequest userLoginRequest, HttpSession session) {
        log.debug("begin");

        loginService.login(session, userLoginRequest);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.debug("begin");

        loginService.logout(session);

        return "redirect:/";
    }

    @PutMapping("/users/{userId}")
    public String editUser(@PathVariable("userId") Long userId, HttpSession session, @Valid UserEditRequest userEditRequest, BindingResult bindingResult) {
        log.debug("begin");

        if (bindingResult.hasErrors()) {
            return "mypage-edit";
        }
        User user = userService.editUserName(userId, userEditRequest.getName());
        session.setAttribute(USER, user);

        return "redirect:/";
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId, HttpSession session) {
        log.debug("begin");

        userService.deleteById(userId);
        log.info("userId: {} ", userId);

        loginService.logout(session);
        return "redirect:/";
    }
}
