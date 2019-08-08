package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.CommentService;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.web.dto.UserResponse;
import techcourse.myblog.application.exception.JsonAPIException;
import techcourse.myblog.domain.Comment;

import javax.servlet.http.HttpSession;
import java.net.URI;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentAPIController {
    private static final Logger log = LoggerFactory.getLogger(CommentAPIController.class);

    private final CommentService commentService;

    public CommentAPIController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity writeComment(@PathVariable Long articleId,
                                       @RequestBody CommentRequest commentRequest,
                                       HttpSession httpSession) {
        try {
            UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
            Comment comment = commentService.save(commentRequest, articleId, userResponse.getId());
            return ResponseEntity.created(URI.create(String.format("/articles/%d/comments/%d", articleId, comment.getId())))
                .body(comment);
        } catch (RuntimeException e) {
            log.error("Error while write comment", e);
            throw new JsonAPIException(e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId,
                                                      HttpSession httpSession) {
        try {
            UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
            commentService.delete(commentId, userResponse.getId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error while delete comment", e);
            throw new JsonAPIException(e.getMessage());
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable("commentId") Long commentId,
                                                      @RequestBody CommentRequest commentRequest,
                                                      HttpSession httpSession) {
        try {
            UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
            commentService.updateComment(commentId, userResponse.getId(), commentRequest);
            Comment comment = commentService.findById(commentId);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            log.error("Error while update comment", e);
            throw new JsonAPIException(e.getMessage());
        }
    }
}
