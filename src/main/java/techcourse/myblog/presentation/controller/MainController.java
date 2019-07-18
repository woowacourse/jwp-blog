package techcourse.myblog.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.CategoryRepository;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public MainController(final ArticleRepository articleRepository, final CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public String readHomePage(HttpSession httpSession, Model model) {
        model.addAttribute("articles", articleRepository.findAll());

        if (httpSession.getAttribute("email") != null) {
            model.addAttribute("email", httpSession.getAttribute("email"));
        }

        return "/index";
    }

    @GetMapping("/writing")
    public String readWritingPage(Model model) {
        model.addAttribute("method", "post");
        return "/article-edit";
    }

    @GetMapping("/login")
    public String readLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String readSignUpPage() {
        return "signup";
    }
}
