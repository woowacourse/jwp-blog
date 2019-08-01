package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.ArticleInputException;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.NotMatchAuthenticationException;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class ArticleController {
    public static final String ARTICLE_DEFAULT_URL = "/articles";

    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(final ArticleService articleService, final CommentService commentService) {
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

    @PostMapping(ARTICLE_DEFAULT_URL)
    public RedirectView createArticle(@Valid ArticleDto articleDto, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            throw new ArticleInputException("입력값이 잘못되었습니다.");
        }

        User user = (User) session.getAttribute("user");
        articleDto.setAuthor(user);
        Article article = articleService.create(articleDto);

        return new RedirectView(ARTICLE_DEFAULT_URL + "/" + article.getId());
    }

    @GetMapping(ARTICLE_DEFAULT_URL + "/{articleId}")
    public String readArticle(@PathVariable Long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", commentService.findAllByArticleId(articleId));

        return "article";
    }

    @GetMapping(ARTICLE_DEFAULT_URL + "/{articleId}/edit")
    public String showArticleEditPage(@PathVariable Long articleId, Model model, HttpSession session) {
        Article article = articleService.findById(articleId, (User) session.getAttribute("user"));
        model.addAttribute("article", article);

        return "article-edit";
    }

    @PutMapping(ARTICLE_DEFAULT_URL + "/{articleId}")
    public RedirectView updateArticle(@PathVariable Long articleId, ArticleDto articleDto, HttpSession session) {
        Article updateArticle = articleService.update(articleDto, articleId, (User) session.getAttribute("user"));

        return new RedirectView(ARTICLE_DEFAULT_URL + "/" + updateArticle.getId());
    }

    @DeleteMapping(ARTICLE_DEFAULT_URL + "/{articleId}")
    public RedirectView deleteArticle(@PathVariable Long articleId, HttpSession session) {
        articleService.delete(articleId, (User) session.getAttribute("user"));
        return new RedirectView("/");
    }

    @ExceptionHandler({NotMatchAuthenticationException.class, ArticleNotFoundException.class, ArticleInputException.class})
    public RedirectView articleException(Exception exception) {
        return new RedirectView("/");
    }

    @PostMapping(ARTICLE_DEFAULT_URL + "/{articleId}/comments")
    public RedirectView createComment(@PathVariable final Long articleId, HttpSession session, Comment comment) {
        commentService.create(articleId, (User) session.getAttribute("user"), comment);
        return new RedirectView(ARTICLE_DEFAULT_URL + "/" + articleId);
    }

    @DeleteMapping(ARTICLE_DEFAULT_URL + "/{articleId}/comments/{commentId}")
    public RedirectView deleteComment(@PathVariable final Long articleId, HttpSession session, @PathVariable final Long commentId) {
        commentService.delete(commentId, (User) session.getAttribute("user"));
        return new RedirectView(ARTICLE_DEFAULT_URL + "/" + articleId);
    }

    @PutMapping(ARTICLE_DEFAULT_URL + "/{articleId}/comments/{commentId}")
    public RedirectView updateComment(String content, @PathVariable final Long articleId, @PathVariable final Long commentId, HttpSession session) {
        commentService.update(content, commentId, (User) session.getAttribute("user"));
        return new RedirectView(ARTICLE_DEFAULT_URL + "/" + articleId);
    }
}