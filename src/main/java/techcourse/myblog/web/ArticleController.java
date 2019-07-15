package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
public class ArticleController {
    private ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticlePage(Article article, Model model) {
        articleRepository.save(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/")
    public String showArticlesPage(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showArticleEditingPage(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleRepository.findById(articleId));
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleByIdPage(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleRepository.findById(articleId));
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticleByIdPage(@PathVariable int articleId, Article article, Model model) {
        articleRepository.update(articleId, article);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticleByIdPage(@PathVariable int articleId) {
        articleRepository.delete(articleId);
        return "redirect:/";
    }
}
