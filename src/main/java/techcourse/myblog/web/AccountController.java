package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserForm;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AccountController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/accounts/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "signup";
    }

    @PostMapping("/accounts/users")
    public String processSignup(Model model, @Valid UserForm userForm, Errors errors) {

        if (errors.hasErrors()) {
            return "signup";
        }

        User user = userForm.toUser(passwordEncoder);
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "0", "이메일 중복입니다.");
            return "signup";
        }

        userRepository.save(user);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
