package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
}
