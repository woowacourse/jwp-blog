package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.ArticleReadService;
import techcourse.myblog.application.ArticleWriteService;
import techcourse.myblog.application.CommentReadService;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.presentation.support.LoginUser;

import javax.validation.groups.Default;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;
    private final CommentReadService commentReadService;
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

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
    public RedirectView createArticle(LoginUser loginUser, @ModelAttribute("/articles/writing") @Validated(Default.class) ArticleDto articleDto) {
        log.debug("article save request data : -> {}", articleDto);
        Article savedArticle = articleWriteService.save(articleDto.toArticle(loginUser.getUser()));
        log.debug("article save response data : -> {}", savedArticle);
        return new RedirectView("/articles/" + savedArticle.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        log.debug("article read request data : -> {}", articleId);
        Article article = articleReadService.findById(articleId);
        log.debug("article read response data : -> {}, {}", article);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(LoginUser loginUser, @PathVariable Long articleId, Model model) {
        log.debug("article edit read request data : -> {}", articleId);
        Article article = articleReadService.findByIdAndAuthor(articleId, loginUser.getUser());
        log.debug("article edit read response data : -> {}", article);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public RedirectView editArticle(LoginUser loginUser, @PathVariable Long articleId, @ModelAttribute("/") @Validated(Default.class) ArticleDto articleDto) {
        log.debug("article modify request data : -> {}, {}", articleId, articleDto);
        articleWriteService.update(articleId, articleDto.toArticle(loginUser.getUser()));
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(LoginUser loginUser, @PathVariable Long articleId) {
        log.debug("article delete request data : -> {}", articleId);
        articleReadService.findByIdAndAuthor(articleId, loginUser.getUser());
        articleWriteService.removeById(articleId);
        return new RedirectView("/");
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(BindException e) {
        return new RedirectView(e.getObjectName());
    }
}