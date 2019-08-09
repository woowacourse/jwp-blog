package techcourse.myblog.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.web.annotation.LoginUser;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final String ARTICLE_BASE_URL = "/articles/";
    private static final String ROOT_URL = "/";

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("")
    public RedirectView createArticle(@Valid ArticleDto newArticleDto, @LoginUser User loginUser) {
        log.info("user : {}", loginUser);
        log.info("new ArticleData : {}", newArticleDto);

        Article article = articleService.save(newArticleDto.toEntity(loginUser));

        return new RedirectView(ARTICLE_BASE_URL + article.getId());
    }

    @GetMapping("/{articleId}")
    public String selectArticle(@PathVariable long articleId, Model model) {
        log.info("articleId : {}", articleId);

        Article article = articleService.findById(articleId);
        model.addAttribute(article);

        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String moveArticleEditPage(@PathVariable long articleId, Model model, @LoginUser User loginUser) {
        log.info("articleId : {}", articleId);
        log.info("user : {}", loginUser);

        Article article = articleService.findById(articleId, loginUser.getId());
        model.addAttribute(article);

        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public RedirectView updateArticle(@PathVariable long articleId, @Valid ArticleDto updateArticleDto, @LoginUser User loginUser, Model model) {
        log.info("user : {}", loginUser);
        log.info("articleId : {}", articleId);
        log.info("updatedArticleDto : {}", updateArticleDto);

        Article article = articleService.update(articleId, updateArticleDto.toEntity(loginUser));
        model.addAttribute(article);

        return new RedirectView(ARTICLE_BASE_URL + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(@PathVariable long articleId, @LoginUser User loginUser) {
        log.info("articleId : {}", articleId);
        log.info("user : {}", loginUser);

        Article article = articleService.findById(articleId, loginUser.getId());
        articleService.deleteById(article.getId());

        return new RedirectView(ROOT_URL);
    }
}
