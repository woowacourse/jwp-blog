package techcourse.myblog.article.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.article.dto.ArticleCreateDto;
import techcourse.myblog.article.dto.ArticleResponseDto;
import techcourse.myblog.article.dto.ArticleUpdateDto;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.article.service.ArticleService;
import techcourse.myblog.comment.service.CommentService;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView createArticle(ArticleCreateDto articleDto, UserSession userSession) {
        long newArticleId = articleService.save(articleDto, userSession.getId()).getId();
        return new RedirectView("/articles/" + newArticleId);
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable long articleId, Model model) {
        model.addAttribute("article", articleService.find(articleId));
        model.addAttribute("comments", commentService.findAllByArticleId(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable long articleId, Model model, UserSession userSession) {
        ArticleResponseDto articleResponse = articleService.find(articleId, userSession.getId());
        model.addAttribute("article", articleResponse);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView updateArticle(@PathVariable long articleId, ArticleUpdateDto articleDto, UserSession userSession) {
        long updatedArticleId = articleService.update(articleId, articleDto, userSession.getId()).getId();
        return new RedirectView("/articles/" + updatedArticleId);
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable long articleId, UserSession userSession) {
        articleService.deleteById(articleId, userSession.getId());
        return new RedirectView("/");
    }
}
