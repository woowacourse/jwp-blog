package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.domain.*;
import techcourse.myblog.service.ArticleService;

import java.util.List;

import static techcourse.myblog.controller.ArticleController.ARTICLE_URL;

@Slf4j
@Controller
@RequestMapping(ARTICLE_URL)
public class ArticleController {
    private final ArticleService articleService;
    public static final String ARTICLE_URL = "/articles";

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping
    public String saveArticlePage(ArticleDto articleDto, User user) {
        log.debug(">>> save article : {}, user : {}", articleDto, user);
        Article article = articleService.saveArticle(articleDto.toArticle(user));
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("{articleId}/edit")
    public String showArticleEditingPage(@PathVariable long articleId, Model model, User user) {
        log.debug(">>> article Id : {}", articleId);
        Article article = articleService.findArticleById(articleId);
        if (article.isNotMatchAuthor(user)) {
            return "redirect:/";
        }

        model.addAttribute("article", article);
        return "article-edit";
    }

    @GetMapping("{articleId}")
    public String showArticleByIdPage(@PathVariable long articleId, Model model) {
        log.debug(">>> article Id : {}", articleId);
        Article article = articleService.findArticleById(articleId);
        List<Comment> comments = articleService.findAllCommentsByArticleId(articleId);

        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "article";
    }

    @PutMapping("{articleId}")
    public String updateArticleByIdPage2(@PathVariable long articleId, ArticleDto articleDto, User user) {
        log.debug(">>> put article Id : {}, ArticleDto : {}, user : {}", articleId, articleDto, user);
        articleDto.setId(articleId);

        if (articleService.isSuccessUpdate(articleDto, user)) {
            return "redirect:/articles/" + articleId;
        }
        return "redirect:/";
    }

    @DeleteMapping("{articleId}")
    public String deleteArticleByIdPage(@PathVariable long articleId, User user) {
        log.debug(">>> article Id : {}", articleId);
        Article article = articleService.findArticleById(articleId);
        if (article.isNotMatchAuthor(user)) {
            return "redirect:/";
        }
        articleService.deleteArticle(articleId);

        return "redirect:/";
    }
}
