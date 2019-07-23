package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import techcourse.myblog.controller.dto.LoginDTO;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.exception.UserFromException;
import techcourse.myblog.model.User;
import techcourse.myblog.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@SessionAttributes("user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(String loginError, Model model) {
        model.addAttribute("loginError", loginError);
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm() {
        return "signup";
    }

    @PostMapping
    public String create(@Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
            throw new UserFromException(bindingResult.getFieldError().getDefaultMessage());
        }
        userService.save(userDTO);
        return "redirect:/users/login";
    }

    @PostMapping("/login")
    public String login(LoginDTO loginDTO, Model model) {
        User user = userService.getLoginUser(loginDTO);
        model.addAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        log.error("userService : {}", userService.getUsers().get(0).getUserName());
        return "user-list";
    }

    @DeleteMapping
    public String deleteUser(@ModelAttribute User user, SessionStatus sessionStatus) {
        userService.delete(user);
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String myPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "mypage";
    }

    @PostMapping("/mypage")
    public String updateProfile(@Valid UserDTO userDTO, Model model) {
        User user = userService.update(userDTO);
        model.addAttribute("user", user);
        return "redirect:/users/mypage";
    }

    @GetMapping("/mypage-edit")
    public String myPageEdit(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "mypage-edit";
    }
}
