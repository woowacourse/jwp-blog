package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public RedirectView createComment(@PathVariable final Long articleId, HttpSession session, String content) {
        commentService.create(articleId, (User) session.getAttribute("user"), content);
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{commentId}")
    public RedirectView deleteComment(@PathVariable final Long articleId, @PathVariable final Long commentId,
                                      HttpSession session) {
        commentService.delete(commentId, (User) session.getAttribute("user"));
        return new RedirectView("/articles/" + articleId);
    }

    @PutMapping("/{commentId}")
    public RedirectView updateComment(@PathVariable final Long articleId, @PathVariable final Long commentId,
                                      HttpSession session, String content) {
        commentService.update(commentId, (User) session.getAttribute("user"), content);
        return new RedirectView("/articles/" + articleId);
    }
}
