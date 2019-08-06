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

import javax.servlet.http.HttpSession;
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
    public ResponseEntity<Comment> saveComment(@RequestBody @Valid CommentRequest commentRequest, HttpSession httpSession) {
        log.debug("begin");

        Comment comment = commentService.save(commentRequest, (User) httpSession.getAttribute("user"));
        log.info("comment: {}", comment);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> editComment(@RequestBody @Valid CommentRequest commentRequest, @PathVariable("commentId") Long commentId, HttpSession httpSession) {
        log.debug("begin");

        Comment updatedComment = commentService.update(commentRequest, (User) httpSession.getAttribute("user"), commentId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId, HttpSession httpSession) {
        log.debug("begin");

        // TODO: 2019-08-03 HttpSession -> ArgumentResolver
        commentService.deleteById(commentId, (User) httpSession.getAttribute("user"));
        log.info("commentId: {}", commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
