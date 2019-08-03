package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.ArticleService;
import techcourse.myblog.application.service.CommentService;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;
    private final CommentService commentService;

    @Autowired
    public ArticleController(final ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @PostMapping("/articles")
    public RedirectView createArticles(ArticleDto article, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Long id = articleService.save(article, user);

        return new RedirectView("/articles/" + id);
    }

    @GetMapping("/articles/{articleId}")
    public ModelAndView readArticlePageByArticleId(@PathVariable Long articleId, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("/article");
        ArticleDto article = articleService.findById(articleId);
        User userFromSession = (User) httpSession.getAttribute("user");
        modelAndView.addObject("article", article);
        modelAndView.addObject("isAuthor", articleService.matchAuthor(article, userFromSession));
        List<CommentDto> comments = commentService.findAllCommentsByArticleId(articleId);
        List<Boolean> checkAuthor = commentService.matchAuthor(comments, userFromSession);
        modelAndView.addObject("comments", comments);
        modelAndView.addObject("checkCommentsAuthor", checkAuthor);
        return modelAndView;
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView readArticleEditPage(@PathVariable Long articleId, HttpSession httpSession) {
        articleService.matchAuthor(articleId, (User) httpSession.getAttribute("user"));
        ModelAndView modelAndView = new ModelAndView("article-edit");
        modelAndView.addObject("article", articleService.findById(articleId));
        modelAndView.addObject("method", "put");
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView updateArticle(@PathVariable Long articleId, ArticleDto article, HttpSession httpSession) {
        RedirectView redirectView = new RedirectView("/articles/" + articleId);
        articleService.modify(articleId, article, (User) httpSession.getAttribute("user"));
        return redirectView;
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable Long articleId, HttpSession httpSession) {
        RedirectView redirectView = new RedirectView("/");
        articleService.removeById(articleId, (User) httpSession.getAttribute("user"));
        return redirectView;
    }
}