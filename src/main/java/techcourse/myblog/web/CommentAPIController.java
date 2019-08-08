package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.CommentService;
import techcourse.myblog.application.dto.BaseResponse;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.CommentResponse;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.JsonAPIException;
import techcourse.myblog.domain.Comment;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentAPIController {
    private static final Logger log = LoggerFactory.getLogger(CommentAPIController.class);

    private final CommentService commentService;

    public CommentAPIController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> writeComment(@PathVariable Long articleId,
                                                     @RequestBody CommentRequest commentRequest,
                                                     HttpSession httpSession) {
        try {
            UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
            Comment comment = commentService.save(commentRequest, articleId, userResponse.getId());
            return new ResponseEntity<>(new CommentResponse(comment), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error("Error while write comment", e);
            throw new JsonAPIException(e.getMessage());
        }

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<BaseResponse> deleteComment(@PathVariable("commentId") Long commentId,
                                                      HttpSession httpSession) {
        try {
            UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
            commentService.delete(commentId, userResponse.getId());
            return new ResponseEntity<>(new BaseResponse("ok"), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error while delete comment", e);
            throw new JsonAPIException(e.getMessage());
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<BaseResponse> updateComment(@PathVariable("commentId") Long commentId,
                                                      @RequestBody CommentRequest commentRequest,
                                                      HttpSession httpSession) {
        try {
            UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
            commentService.updateComment(commentId, userResponse.getId(), commentRequest);
            Comment comment = commentService.findById(commentId);
            return new ResponseEntity<>(new CommentResponse(comment), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error while update comment", e);
            throw new JsonAPIException(e.getMessage());
        }
    }
}
