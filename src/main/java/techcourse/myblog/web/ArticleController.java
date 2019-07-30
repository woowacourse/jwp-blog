package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class ArticleController {
    private final UserService userService;
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService, UserService userService) {
        this.userService = userService;
        this.articleService = articleService;
        this.commentService = commentService;
    }

    private User getCurrentUser(HttpSession session) {
        return userService.getUserByEmail((String) session.getAttribute("email"));
    }


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.loadEveryArticles());
        return "index";
    }

    @GetMapping("/writing")
    public String writingForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView write(String title, String coverUrl, String contents, HttpSession session) {
        return new RedirectView("/articles/" + articleService.write(
                new Article(getCurrentUser(session), title, coverUrl, contents))
        );
    }

    @GetMapping("/articles/{articleId}")
    public String read(@PathVariable long articleId, Model model) {
        return articleService.maybeArticle(articleId).map(article -> {
            model.addAttribute("article", article);
            model.addAttribute("comments", article.getComments());
            return "article";
        }).orElse("redirect:/");
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateForm(@PathVariable long articleId, Model model, HttpSession session) {
        return articleService.maybeArticle(articleId).map(article -> {
            if (article.isSameAuthor(getCurrentUser(session))) {
                model.addAttribute("article", article);
                return "article-edit";
            }
            return "redirect:/";
        }).orElse("redirect:/");
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView update(
            @PathVariable long articleId,
            String title,
            String coverUrl,
            String contents,
            HttpSession session
    ) {
        return articleService.tryUpdate(articleId, new Article(getCurrentUser(session), title, coverUrl, contents))
                ? new RedirectView("/articles/" + articleId)
                : new RedirectView("/");
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView delete(@PathVariable long articleId, HttpSession session) {
        articleService.tryDelete(articleId, getCurrentUser(session));
        return new RedirectView("/");
    }

    @PostMapping("/articles/{articleId}/comment")
    public RedirectView writeComment(@PathVariable long articleId, String contents, HttpSession session) {
        commentService.write(articleService.maybeArticle(articleId).get(), getCurrentUser(session), contents);
        return new RedirectView("/articles/" + articleId);
    }

    @PutMapping("/articles/{articleId}/comment/{commentId}")
    public RedirectView updateComment(
            @PathVariable long articleId,
            @PathVariable long commentId,
            String contents,
            HttpSession session
    ) {
        commentService.tryUpdate(commentId, contents, getCurrentUser(session));
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}/comment/{commentId}")
    public RedirectView deleteComment(
            @PathVariable long articleId,
            @PathVariable long commentId,
            HttpSession session
    ) {
        commentService.delete(commentId, getCurrentUser(session));
        return new RedirectView("/articles/" + articleId);
    }
}