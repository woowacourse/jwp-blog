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
import techcourse.myblog.support.validator.UserSession;

import javax.servlet.http.HttpSession;
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
    public String createLoginForm(HttpSession httpSession, UserLoginRequest userLoginRequest) {
        log.debug("begin");

        if (httpSession.getAttribute(USER) == null) {
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
    public String myPageForm(Model model, @UserSession User sessionUser) {
        log.debug("begin");

        model.addAttribute(USER, sessionUser);
        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String myPageEditForm(UserEditRequest userEditRequest) {
        return "mypage-edit";
    }

    @PostMapping("/login")
    public String login(UserLoginRequest userLoginRequest, HttpSession httpSession) {
        log.debug("begin");

        User user = userService.findUserByEmail(userLoginRequest);
        httpSession.setAttribute(USER, user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        log.debug("begin");

        httpSession.removeAttribute(USER);
        return "redirect:/";
    }

    @PutMapping("/users/{userId}")
    public String editUser(@PathVariable("userId") Long userId, HttpSession httpSession, @Valid UserEditRequest userEditRequest, BindingResult bindingResult) {
        log.debug("begin");

        if (bindingResult.hasErrors()) {
            log.debug("error : {}", bindingResult.getFieldError().getDefaultMessage());
            return "mypage-edit";
        }

        User user = userService.editUserName(userId, userEditRequest);
        httpSession.setAttribute(USER, user);
        return "redirect:/mypage";
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId, HttpSession httpSession) {
        log.debug("begin");

        userService.deleteById(userId);
        log.info("userId: {} ", userId);

        httpSession.removeAttribute(USER);
        return "redirect:/";
    }
}
