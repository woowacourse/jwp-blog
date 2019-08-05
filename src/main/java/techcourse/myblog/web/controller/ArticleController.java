package techcourse.myblog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.web.support.SessionInfo;
import techcourse.myblog.web.support.UserSessionInfo;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleService.findArticle(articleId));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleService.findArticle(articleId));
        return "article-edit";
    }

    @PostMapping
    public String saveArticle(ArticleDto dto,
                              @SessionInfo UserSessionInfo userSessionInfo) {
        return "redirect:/articles/" + articleService.save(dto, userSessionInfo.toUser()).getId();
    }

    @PutMapping("/{articleId}")
    public String getModifiedArticle(@PathVariable long articleId,
                                     @SessionInfo UserSessionInfo userSessionInfo,
                                     ArticleDto dto,
                                     Model model) {
        setArticleModel(model, articleService.update(articleId, dto, userSessionInfo.toUser()));
        return "article";
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable long articleId,
                                @SessionInfo UserSessionInfo userSessionInfo) {
        articleService.delete(articleId, userSessionInfo.toUser());
        return "redirect:/";
    }

    private void setArticleModel(Model model, Article article) {
        model.addAttribute("article", article);
    }
}