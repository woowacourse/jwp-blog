package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.CommentService;
import techcourse.myblog.application.dto.BaseResponse;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.CommentResponse;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.domain.Comment;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentAPIController {

    private final CommentService commentService;

    public CommentAPIController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> writeComment(@PathVariable Long articleId,
                                                     @RequestBody CommentRequest commentRequest,
                                                     HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        Comment comment = commentService.save(commentRequest, articleId, userResponse.getId());

        return new ResponseEntity<>(new CommentResponse(comment), HttpStatus.CREATED);
    }





}
