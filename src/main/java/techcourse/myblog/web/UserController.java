package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class UserController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        return "signup";
    }
}