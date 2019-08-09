package techcourse.myblog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.ArticleResponseDto;
import techcourse.myblog.service.ArticleGenericService;
import techcourse.myblog.web.support.SessionInfo;
import techcourse.myblog.web.support.UserSessionInfo;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleGenericService articleGenericService;

    public ArticleController(ArticleGenericService articleGenericService) {
        this.articleGenericService = articleGenericService;
    }

    @GetMapping("/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleGenericService.findArticle(articleId, ArticleResponseDto.class));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleGenericService.findArticle(articleId, ArticleResponseDto.class));
        return "article-edit";
    }

    @PostMapping
    public String saveArticle(ArticleDto dto,
                              @SessionInfo UserSessionInfo userSessionInfo) {
        return "redirect:/articles/" + articleGenericService.add(dto, userSessionInfo.toUser(), Article.class).getId();
    }

    @PutMapping("/{articleId}")
    public String getModifiedArticle(@PathVariable long articleId,
                                     @SessionInfo UserSessionInfo userSessionInfo,
                                     ArticleDto dto,
                                     Model model) {
        setArticleModel(model, articleGenericService.update(articleId, dto, userSessionInfo.toUser(), ArticleResponseDto.class));
        return "article";
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable long articleId,
                                @SessionInfo UserSessionInfo userSessionInfo) {
        articleGenericService.delete(articleId, userSessionInfo.toUser());
        return "redirect:/";
    }

    private void setArticleModel(Model model, ArticleResponseDto dto) {
        model.addAttribute("article", dto);
    }
}