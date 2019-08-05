package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.application.service.ArticleService;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    
    private final ArticleService articleService;

    public MainController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String readHomePage(HttpSession httpSession, Model model) {
        model.addAttribute("articles", articleService.findAll());

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
