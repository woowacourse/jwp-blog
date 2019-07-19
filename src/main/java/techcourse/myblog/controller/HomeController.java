package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.UserDto;

@Controller
public class HomeController {
    private final ArticleRepository articleRepository;

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

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
    public String loginForm() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm(UserDto userDto) {
        return "signup";
    }
}
