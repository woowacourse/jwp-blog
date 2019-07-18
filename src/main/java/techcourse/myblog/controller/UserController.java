package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.controller.dto.LoginDTO;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserNotExistException;
import techcourse.myblog.model.User;
import techcourse.myblog.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginForm(String loginError, Model model) {
        model.addAttribute("loginError", loginError);
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm(String signUpStatus, Model model){
        model.addAttribute("signUpStatus", signUpStatus);
        return "signup";
    }

    @PostMapping
    public String create(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.save(userDTO);
            return "redirect:/users/login";
        } catch (EmailRepetitionException e) {
            redirectAttributes.addAttribute("signUpStatus", e.getMessage());
            return "redirect:/users/signup";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginDTO, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getLoginUser(loginDTO);
            return "redirect:/";
        } catch (UserNotExistException | LoginFailException e) {
            redirectAttributes.addAttribute("loginError", e.getMessage());
            return "redirect:/users/login";
        }
    }

    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "user-list";
    }

    @DeleteMapping
    public String deleteUser(UserDTO userDTO, Model model) {
        userService.delete(userDTO);
        return "redirect:/";
    }
}
