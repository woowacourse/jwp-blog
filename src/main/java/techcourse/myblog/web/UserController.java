package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;

@Controller
public class UserController {

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @GetMapping("/users")
    public String showAllUsers() {
        return "user-list";
    }

    @PostMapping("/users")
    public RedirectView registerUser(User user) {
        return new RedirectView("/login");
    }
}