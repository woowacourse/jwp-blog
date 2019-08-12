package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.ArticleRequest;
import techcourse.myblog.support.validator.UserSession;

import javax.validation.Valid;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/edit")
    public String formArticle(Model model) {
        log.debug("begin");

        model.addAttribute("article", null);
        return "article-edit";
    }

    @PostMapping
    public String saveArticle(@Valid ArticleRequest articleRequest, Model model, @UserSession User sessionUser) {
        log.debug("begin");

        Article article = articleService.save(articleRequest, sessionUser);
        log.info("article: {}", article);

        model.addAttribute("article", article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/{articleId}")
    public String showArticlePage(@PathVariable("articleId") long articleId, Model model) {
        log.debug("begin");

        model.addAttribute("articleId", articleId);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String edit(@PathVariable("articleId") long articleId, Model model, @UserSession User sessionUser) {
        log.debug("begin");

        Article article = articleService.findByIdWithUser(articleId, sessionUser);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String editArticle(@PathVariable("articleId") long articleId, @ModelAttribute ArticleRequest articleRequest,
                              @UserSession User sessionUser, Model model) {
        log.debug("begin");

        Article article = articleService.editArticle(articleRequest, articleId, sessionUser);
        model.addAttribute("article", article);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable("articleId") long articleId, @UserSession User sessionUser) {
        log.debug("begin");

        articleService.deleteById(articleId, sessionUser);
        log.info("articleId: {}", articleId);

        return "redirect:/";
    }
}
