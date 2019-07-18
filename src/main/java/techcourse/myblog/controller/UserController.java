package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginForm(){
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

    @GetMapping
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        return "user-list";
    }

}
