package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.ArticleReadService;
import techcourse.myblog.application.ArticleWriteService;
import techcourse.myblog.application.CommentReadService;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleFeature;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.presentation.support.LoginUser;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;
    private final CommentReadService commentReadService;

    public ArticleController(ArticleReadService articleReadService,
                             ArticleWriteService articleWriteService,
                             CommentReadService commentReadService) {
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
        this.commentReadService = commentReadService;
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(LoginUser loginUser, ArticleFeature articleFeature) {
        log.debug("article save request data : -> {}", articleFeature);
        Article savedArticle = articleWriteService.save(articleFeature.toArticle(loginUser.getUser()));
        log.debug("article save response data : -> {}", savedArticle);
        return new RedirectView("/articles/" + savedArticle.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable Long articleId, Model model) {
        log.debug("article read request data : -> {}", articleId);
        Article article = articleReadService.findById(articleId);
        List<Comment> comments = commentReadService.findByArticleId(articleId);
        log.debug("article read response data : -> {}, {}", article, comments);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(LoginUser loginUser, @PathVariable Long articleId, Model model) {
        log.debug("article edit read request data : -> {}", articleId);
        Article article = articleReadService.findById(articleId);
        article.validateAuthor(loginUser.getUser());
        log.debug("article edit read response data : -> {}", article);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public RedirectView editArticle(LoginUser loginUser, @PathVariable Long articleId, ArticleFeature articleFeature) {
        log.debug("article modify request data : -> {}, {}", articleId, articleFeature);
        articleWriteService.update(articleId, articleFeature, loginUser.getUser());
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(LoginUser loginUser, @PathVariable Long articleId) {
        log.debug("article delete request data : -> {}", articleId);
        articleReadService.findById(articleId).validateAuthor(loginUser.getUser());
        articleWriteService.removeById(articleId);
        return new RedirectView("/");
    }
}