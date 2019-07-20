package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;

@Controller
public class IndexController {
    private final ArticleService articleService;

    @Autowired
    public IndexController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        Iterable<Article> articles = articleService.findAllArticles();
        model.addAttribute("articles", articles);
        return "index";
    }
}
