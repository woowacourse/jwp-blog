package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.CommentService;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.CommentResponse;
import techcourse.myblog.application.dto.UserResponse;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity<CommentResponse> save(@PathVariable("articleId") Long articleId,
                                                @RequestBody CommentRequest commentRequest,
                                                HttpSession httpSession) {
        log.info(commentRequest.getContents());

        UserResponse user = (UserResponse) httpSession.getAttribute("user");
        CommentResponse commentResponse = commentService.save(commentRequest, articleId, user.getId());
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> modify(@PathVariable("commentId") Long commentId,
                                  @RequestBody CommentRequest commentRequest, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        CommentResponse commentResponse = commentService.modify(commentId, userResponse, commentRequest);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> remove(@PathVariable("commentId") Long commentId, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        commentService.remove(commentId, userResponse);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
