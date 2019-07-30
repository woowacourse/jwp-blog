package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;

import java.util.List;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String indexView(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String writeArticleView() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String publishArticle(ArticleRequestDto article) {
        articleService.addArticle(Article.from(article));
        return "redirect:/";
    }

    @GetMapping("/articles/{articleId}")
    public String articleView(@PathVariable Long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticleView(@PathVariable Long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable Long articleId, ArticleRequestDto reqArticle, Model model) {
        Article article = Article.from(reqArticle);
        article.setId(articleId);
        articleService.update(article);
        Article articleToShow = articleService.findById(articleId);
        model.addAttribute("article", articleToShow);
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleService.deleteById(articleId);
        return "redirect:/";
    }
}
