package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.service.ArticleService;

@Controller
public class HomeController {
    private final ArticleService articleService;

    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("articleDtos", articleService.getAllArticles());
        return "index";
    }
}
