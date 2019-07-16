package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String goIndex() {
        return "index";
    }

    @GetMapping("/articles")
    public String goArticles() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(Article article, Model model) {
        articleRepository.save(article);
        model.addAttribute("article", article);
        return "article";
    }
}
