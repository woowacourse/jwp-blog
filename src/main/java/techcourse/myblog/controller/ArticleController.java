package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.model.User;
import techcourse.myblog.service.ArticleService;

@Controller
@SessionAttributes("user")
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/writing")
    public String articleForm() {
        return "article-edit";
    }

    @PostMapping
    public String createArticle(ArticleDto articleDto, @ModelAttribute User user) {
        Long newArticleId = articleService.save(articleDto, user);
        return "redirect:/articles/" + newArticleId;
    }

    @GetMapping("/{articleId}")
    public String findArticle(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        model.addAttribute("comments", articleService.findAllComment(articleId));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String articleEditForm(@PathVariable Long articleId, Model model, @ModelAttribute User user) {
        articleService.checkOwner(articleId, user);
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleDto articleDto, @ModelAttribute User user) {
        articleService.update(articleDto, user, articleId);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId, @ModelAttribute User user) {
        articleService.checkOwner(articleId, user);

        articleService.delete(articleId);
        return "redirect:/";
    }
}

