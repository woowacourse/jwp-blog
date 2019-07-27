package techcourse.myblog.web;

import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.article.Article;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.ArticleDto;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private ArticleService articleService;

    ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/new")
    public String writeArticle() {
        return "article-edit";
    }

    @PostMapping("/new")
    public String createArticle(ArticleDto articleDto, Model model) {
        Article article = articleService.createArticle(articleDto);
        model.addAttribute("article", article);
        return "redirect:/articles/" + article.getArticleId();
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable Long articleId, Model model) {
        Article article = articleService.findArticle(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String showUpdateArticle(@PathVariable Long articleId, Model model) {
        Article article = articleService.findArticle(articleId);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleDto updateArticleDto, Model model) {
        Article updateArticle = articleService.updateArticle(articleId, updateArticleDto.toEntity());
        model.addAttribute("article", updateArticle);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return "redirect:/";
    }

}
