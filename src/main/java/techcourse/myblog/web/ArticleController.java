package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {

    private ArticleRepository articleRepository;

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

    @PostMapping("/write")
    public String writeArticle(final Article article) {
        Long newArticleId = articleRepository.generateNewId();
        article.setArticleId(newArticleId);
        articleRepository.addArticle(article);

        return "redirect:/articles/" + newArticleId;
    }

    @PutMapping("/update")
    public String updateArticle(final Article article) {
        articleRepository.updateArticle(article);
        return "redirect:/articles/" + article.getArticleId();
    }

    @GetMapping("/articles/{articleId}")
    public String findArticleById(@PathVariable Long articleId, Model model) {
        articleRepository.findArticleById(articleId).ifPresent(article -> model.addAttribute("article", article));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable Long articleId, Model model) {
        articleRepository.findArticleById(articleId).ifPresent(article -> model.addAttribute("article", article));
        return "article-edit";
    }

    @DeleteMapping("/articles/{articleId}/delete")
    public String deleteArticle(@PathVariable Long articleId) {
        articleRepository.deleteArticle(articleId);
        return "redirect:/";
    }

}
