package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.ModelSupport;

@Controller
public class HomeController {
    private final ArticleRepository articleRepository;

    public HomeController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        ModelSupport.addObjectDoesNotContain(model, "userDto", new UserDto("", "", ""));
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        ModelSupport.addObjectDoesNotContain(model, "userDto", new UserDto("", "", ""));
        return "signup";
    }
}
