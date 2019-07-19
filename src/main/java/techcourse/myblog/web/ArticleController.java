package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String getAllArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/new")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle(final Article article) {
        Long latestId = articleRepository.addArticle(article);
        return "redirect:/articles/" + latestId;
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable Long articleId, final Article article) {
        articleRepository.updateArticle(articleId, article);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String findArticleById(@PathVariable Long articleId, Model model) {
        articleRepository.findArticleById(articleId)
                .ifPresent(a -> model.addAttribute("article", a));

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable Long articleId, Model model) {
        articleRepository.findArticleById(articleId)
                .ifPresent(a -> model.addAttribute("article", a));

        return "article-edit";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleRepository.deleteArticle(articleId);
        return "redirect:/";
    }

}
