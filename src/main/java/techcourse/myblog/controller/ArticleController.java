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

    @GetMapping("{articleId}/edit")
    public String showArticleEditingPage(@PathVariable long articleId, Model model, User user) {
        log.debug(">>> article Id : {}", articleId);
        Article article;
        try {
            article =  articleService.find(articleId, user);
        } catch (RuntimeException e) {
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

    @PostMapping
    public String saveArticlePage(ArticleDto articleDto, User user) {
        log.debug(">>> save article : {}, user : {}", articleDto, user);
        Article article = articleService.saveArticle(articleDto.toArticle(user));
        return "redirect:/articles/" + article.getId();
    }

    @PutMapping("{articleId}")
    public String updateArticleByIdPage(@PathVariable long articleId, ArticleDto articleDto, User user) {
        log.debug(">>> put article Id : {}, ArticleDto : {}, user : {}", articleId, articleDto, user);
        try {
            articleService.update(articleDto, articleId, user);
        } catch (RuntimeException e) {
            return "redirect:/";
        }

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("{articleId}")
    public String deleteArticleByIdPage(@PathVariable long articleId, User user) {
        log.debug(">>> article Id : {}", articleId);
        articleService.delete(articleId, user);
        return "redirect:/";
    }
}
