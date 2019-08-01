package techcourse.myblog.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.ArticleService;
import techcourse.myblog.application.service.CommentService;
import techcourse.myblog.application.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public ArticleController(final ArticleService articleService, CommentService commentService, UserService userService) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/articles")
    public RedirectView createArticles(HttpSession httpSession, ArticleDto article) {
        String email = (String) httpSession.getAttribute("email");
        Long id = articleService.save(article, email);

        return new RedirectView("/articles/" + id);
    }


    //todo 로그인 <-> 세션 비교 중복 제거
    @GetMapping("/articles/{articleId}")
    public ModelAndView readArticlePageByArticleId(@PathVariable Long articleId) {
        ModelAndView modelAndView = new ModelAndView("/article");
        modelAndView.addObject("article", articleService.findById(articleId));
        modelAndView.addObject("user", articleService.findAuthor(articleId));

        List<CommentDto> commentDtos = commentService.findAllByArticleId(articleId);
        modelAndView.addObject("comments", commentDtos);
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView updateArticle(HttpSession httpSession, @PathVariable Long articleId, ArticleDto article) {
        String email = (String) httpSession.getAttribute("email");
        articleService.checkAuthor(articleId, email);
        articleService.modify(articleId, article, email);

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView deleteArticle(HttpSession httpSession, @PathVariable Long articleId) {
        String email = (String) httpSession.getAttribute("email");
        articleService.checkAuthor(articleId, email);

        articleService.removeById(articleId);
        return new RedirectView("/");
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView readArticleEditPage(HttpSession httpSession, @PathVariable Long articleId) {
        String email = (String) httpSession.getAttribute("email");
        articleService.checkAuthor(articleId, email);

        ModelAndView modelAndView = new ModelAndView("/article-edit");
        modelAndView.addObject("article", articleService.findById(articleId));
        modelAndView.addObject("method", "put");

        return modelAndView;
    }
}
