package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

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
        User user = new User(userDTO.getUserName(), userDTO.getEmail(), userDTO.getPassword());
        userRepository.save(user);
        return "redirect:/users/login";
    }

    @GetMapping
    public String showUsers(Model model){
        List<User> users = (List<User>) userRepository.findAll();
        System.out.println(users.size());
        model.addAttribute("users", users);
        return "user-list";
    }

}
