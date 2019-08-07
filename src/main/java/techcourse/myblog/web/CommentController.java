package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequest;
import techcourse.myblog.support.validator.UserSession;

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
    public ResponseEntity<Comment> saveComment(@RequestBody @Valid CommentRequest commentRequest, @UserSession User sessionUser) {
        log.debug("begin");

        Comment comment = commentService.save(commentRequest, sessionUser);
        log.info("Insert comment: {}", comment);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> editComment(@RequestBody @Valid CommentRequest commentRequest, @PathVariable("commentId") Long commentId, @UserSession User sessionUser) {
        log.debug("begin");

        Comment updatedComment = commentService.update(commentRequest, sessionUser, commentId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId, @UserSession User sessionUser) {
        log.debug("begin");

        commentService.deleteById(commentId, sessionUser);
        log.info("Deleted commentId: {}", commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
