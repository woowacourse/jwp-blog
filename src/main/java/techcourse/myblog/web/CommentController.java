package techcourse.myblog.web;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@RestController
public class CommentController {
    private final UserService userService;
    private final ArticleService articleService;
    private final CommentService commentService;

    public CommentController(
            ArticleService articleService,
            CommentService commentService,
            UserService userService
    ) {
        this.userService = userService;
        this.articleService = articleService;
        this.commentService = commentService;
    }

    private User getCurrentUser(HttpSession session) {
        return userService.getUserByEmail((String) session.getAttribute("email"));
    }

    @PostMapping("/articles/{articleId}/comment")
    public CommentDTO writeComment(@PathVariable long articleId, @RequestBody CommentDTO comment, HttpSession session) {
        Comment written = commentService.write(
                articleService.maybeArticle(articleId).get(), getCurrentUser(session), comment.getContents()
        );
        return new CommentDTO(written.getId(), written.getAuthor().getName(), written.getContents(), written.getCreatedTimeAt());
    }

    @PutMapping("/articles/{articleId}/comment/{commentId}")
    public CommentDTO updateComment(
            @PathVariable long articleId,
            @PathVariable long commentId,
            @RequestBody CommentDTO comment,
            HttpSession session
    ) {
        Comment written = commentService.tryUpdate(commentId, comment.getContents(), getCurrentUser(session));
        return new CommentDTO(written.getId(), written.getAuthor().getName(), written.getContents(), written.getCreatedTimeAt());
    }

    @DeleteMapping("/articles/{articleId}/comment/{commentId}")
    public boolean deleteComment(@PathVariable long articleId, @PathVariable long commentId, HttpSession session) {
        commentService.delete(commentId, getCurrentUser(session));
        return true;
    }
}