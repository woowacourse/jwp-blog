package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequest;
import techcourse.myblog.web.argumentResolver.SessionUser;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> saveComment(@RequestBody @Valid CommentRequest commentRequest, @SessionUser User user) {
        log.debug("begin");

        Comment comment = commentService.save(commentRequest, user);
        log.info("comment: {}", comment);

        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> editComment(@RequestBody @Valid CommentRequest commentRequest, @PathVariable("commentId") Long commentId, @SessionUser User user) {
        log.debug("begin");

        Comment updatedComment = commentService.update(commentRequest, user, commentId);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId, @SessionUser User user) {
        log.debug("begin");

        // TODO: 2019-08-03 HttpSession -> ArgumentResolver
        commentService.deleteById(commentId, user);
        log.info("commentId: {}", commentId);

        return ResponseEntity.noContent()
                .build();
    }
}
