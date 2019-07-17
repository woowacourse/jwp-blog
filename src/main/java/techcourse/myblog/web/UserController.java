package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String showSignupForm(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @PostMapping("/users")
    public RedirectView registerUser(User user, RedirectAttributes redirectAttributes) {
        return userRepository.findByEmail(user.getEmail()).map(u -> {
            redirectAttributes.addAttribute("errorMessage", "이미 존재하는 이메일입니다.");
            return new RedirectView("/signup");
        }).orElseGet(() -> {
            userRepository.save(user);
            return new RedirectView("/login");
        });
    }
}