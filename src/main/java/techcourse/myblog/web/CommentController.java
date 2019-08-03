package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.dto.CommentDto;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    private final CommentService commentService;
    private final ArticleService articleService;

    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @GetMapping("/comment/{commentId}/edit")
    public String openCommentEdit(@PathVariable Long commentId, Model model, HttpSession httpSession) {
        commentService.exist(commentId, getLoginUser(httpSession));
        model.addAttribute("comment", commentService.findById(commentId));
        return "comment-edit";
    }

    @PostMapping("/articles/{articleId}/comment")
    public String create(@PathVariable Long articleId, CommentDto commentDto, HttpSession httpSession) {
        Article article = articleService.findById(articleId);
        commentService.save(commentDto, getLoginUser(httpSession), article);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/comment/{commentId}")
    public String updateComment(@PathVariable Long commentId, CommentDto commentDto, HttpSession httpSession) {
        Comment comment = commentService.update(commentId, commentDto, getLoginUser(httpSession));
        log.debug("update comment :  {}", comment);
        return "redirect:/articles/" + comment.getArticleId();
    }

    @DeleteMapping("/comment/{commentId}")
    public String updateComment(@PathVariable Long commentId, HttpSession httpSession) {
        Long articleId = commentService.delete(commentId, getLoginUser(httpSession));
        return "redirect:/articles/" + articleId;
    }

    private User getLoginUser(HttpSession httpSession) {
        return (User) httpSession.getAttribute("user");
    }
}
