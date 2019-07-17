package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AccountController {
    private UserRepository userRepository;

    @Autowired
    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/accounts/signup")
    public String showSignupPage() {
        return "signup";
    }

    @PostMapping("/accounts/users")
    public String processSignup(@Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return "signup";
        }
        userRepository.save(user);
        return "redirect:/";
    }
}
