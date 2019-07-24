package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.ArticleRequest;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ArticleController {
    private static final String ARTICLE_INFO = "article";
    private static final String ARTICLES_INFO = "articles";

    private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = new ArrayList<>();
        articleService.findAll().forEach(articles::add);
        model.addAttribute(ARTICLES_INFO, articles);

        return "index";
    }

    @GetMapping("/writing")
    public String formArticle(Model model) {
        model.addAttribute(ARTICLE_INFO, null);

        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(@Valid ArticleRequest articleRequest, Model model) {
        Article article = articleService.post(articleRequest);
        model.addAttribute(ARTICLE_INFO, article);

        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{articleId}")
    public String selectArticle(@PathVariable("articleId") long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute(ARTICLE_INFO, article);

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String edit(@PathVariable("articleId") long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute(ARTICLE_INFO, article);

        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable("articleId") long articleId, @ModelAttribute ArticleRequest articleRequest, Model model) {
        Article article = articleService.editArticle(articleRequest, articleId);
        model.addAttribute(ARTICLE_INFO, article);

        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable("articleId") long articleId) {
        articleService.deleteById(articleId);

        return "redirect:/";
    }
}
