package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.ArticleReadService;
import techcourse.myblog.application.ArticleWriteService;
import techcourse.myblog.application.dto.ArticleResponseDto;
import techcourse.myblog.domain.article.ArticleFeature;
import techcourse.myblog.presentation.support.LoginUser;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;

    public ArticleController(ArticleReadService articleReadService,
                             ArticleWriteService articleWriteService) {
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(LoginUser loginUser,
                                      ArticleFeature articleFeature) {
        log.debug("article save request data : -> {}", articleFeature);
        ArticleResponseDto savedArticle = articleWriteService.save(articleFeature, loginUser.getUser());
        log.debug("article save response data : -> {}", savedArticle);
        return new RedirectView("/articles/" + savedArticle.getArticleId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(Model model,
                              @PathVariable Long articleId) {
        log.debug("article read request data : -> {}", articleId);
        ArticleResponseDto articleResponseDto = articleReadService.findById(articleId);
        log.debug("article read response data : -> {}", articleResponseDto);
        model.addAttribute("article", articleResponseDto);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(Model model,
                                  LoginUser loginUser,
                                  @PathVariable Long articleId) {
        log.debug("article edit read request data : -> {}", articleId);
        ArticleResponseDto articleResponseDto = articleReadService.findByIdAndValidUser(articleId, loginUser.getUser());
        log.debug("article edit read response data : -> {}", articleResponseDto);
        model.addAttribute("article", articleResponseDto);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public RedirectView editArticle(LoginUser loginUser,
                                    @PathVariable Long articleId,
                                    ArticleFeature articleFeature) {
        log.debug("article modify request data : -> {}, {}", articleId, articleFeature);
        articleWriteService.update(articleId, articleFeature, loginUser.getUser());
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(LoginUser loginUser,
                                      @PathVariable Long articleId) {
        log.debug("article delete request data : -> {}", articleId);
        articleReadService.findByIdAndValidUser(articleId, loginUser.getUser());
        articleWriteService.removeById(articleId);
        return new RedirectView("/");
    }
}