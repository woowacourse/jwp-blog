package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/signup")
    public String signUpForm(){
        return "signup";
    }

    @PostMapping
    public String create(User user) {
        //db 에 삽입
        return "redirect:/users/signup";

    }
}
