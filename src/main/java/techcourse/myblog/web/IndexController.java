package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final ArticleService articleService;

    @GetMapping("/")
    public String index(Model model) {
        Iterable<Article> articles = articleService.findAllArticles();
        model.addAttribute("articles", articles);
        return "index";
    }
}
