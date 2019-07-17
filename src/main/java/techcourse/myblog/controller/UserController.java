package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.UserDTO;
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
    public String signUpForm(){
        return "signup";
    }

    @PostMapping
    public String create(@ModelAttribute UserDTO userDTO) {
        userService.save(userDTO);
        return "redirect:/users/login";
    }

    @GetMapping
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        return "user-list";
    }

}
