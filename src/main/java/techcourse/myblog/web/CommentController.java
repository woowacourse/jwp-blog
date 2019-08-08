package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<CommentDTO> writeComment(@PathVariable long articleId, @RequestBody CommentDTO comment, HttpSession session) {
        Comment written = commentService.write(
                articleService.maybeArticle(articleId).get(), getCurrentUser(session), comment.getContents()
        );
        if (written.getId() > 0) {
            return new ResponseEntity<>(
                    new CommentDTO(written.getId(), written.getAuthor().getName(), written.getContents(), written.getCreatedTimeAt()),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable long commentId, HttpSession session) {
        return commentService.find(commentId).map(comment ->
            new ResponseEntity<>(
                    new CommentDTO(commentId, comment.getAuthor().getName(), comment.getContents(), comment.getCreatedTimeAt()),
                    HttpStatus.OK
            )
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable long commentId, @RequestBody CommentDTO comment, HttpSession session) {
        Comment written = commentService.tryUpdate(commentId, comment.getContents(), getCurrentUser(session));
        return new ResponseEntity<>(
                new CommentDTO(written.getId(), written.getAuthor().getName(), written.getContents(), written.getCreatedTimeAt()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable long commentId, HttpSession session) {
        commentService.delete(commentId, getCurrentUser(session));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}