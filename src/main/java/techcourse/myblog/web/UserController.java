package techcourse.myblog.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/err")
    public String err(){
        return "errorpage";
    }
    @PostMapping("/users")
    public String signUp(User user) {
        if(userRepository.findUserByEmail(user.getEmail()) != null){
            return "redirect:/err";
        }
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "signup";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("user",);
        return "user-list";
    }
}
