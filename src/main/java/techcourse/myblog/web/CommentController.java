package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequest;
import techcourse.myblog.service.dto.CommentResponse;
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

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> selectComment(@PathVariable("commentId") Long commentId) {
        log.debug("begin");
        if (!commentService.isExist(commentId)) {
            return ResponseEntity.noContent()
                    .build();
        }

        return ResponseEntity.ok(commentService.findById(commentId));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> saveComment(@RequestBody @Valid CommentRequest commentRequest, @SessionUser User user) {
        log.debug("begin");

        CommentResponse commentResponse = commentService.save(commentRequest, user);
        return ResponseEntity.ok(commentResponse);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> editComment(@RequestBody @Valid CommentRequest commentRequest,
                                                       @PathVariable("commentId") Long commentId,
                                                       @SessionUser User commenter) {
        log.debug("begin");

        commentService.update(commentRequest, commenter, commentId);
        return ResponseEntity.ok(commentService.findById(commentId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId, @SessionUser User commenter) {
        log.debug("begin");

        if (!commentService.isExist(commentId)) {
            return ResponseEntity.notFound().build();
        }

        if (!commentService.isCommenter(commentId, commenter)) {
            return ResponseEntity.badRequest().build();
        }

        commentService.deleteById(commentId);
        log.info("commentId: {}", commentId);

        return ResponseEntity.noContent()
                .build();
    }
}
