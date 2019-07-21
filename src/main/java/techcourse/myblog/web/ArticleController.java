package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;

    @GetMapping("/")
    public String showArticles(Model model) {
        List<Article> articles = articleService.findAllArticle();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle(ArticleDto articleDto, Model model) {
        Article article = articleDto.toEntity();
        Article newArticle = articleService.saveArticle(article);

        model.addAttribute("article", newArticle);

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showArticleEditingPage(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable int articleId, Model model) {
        try {
            model.addAttribute("article", articleService.findById(articleId));
        } catch (ArticleNotFoundException e) {
            model.addAttribute("message", e);
            return "redirect:/err";
        }
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable int articleId, ArticleDto articleDto, Model model) {
        Article article = articleDto.toEntity();
        article.setId(articleId);

        Article newArticle = articleService.saveArticle(article);
        model.addAttribute("article", newArticle);
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleService.deleteById(articleId);
        return "redirect:/";
    }
}
