package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.exception.CommenterMismatchException;
import techcourse.myblog.web.LoggedInUser;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class RestCommentController {
    private final CommentService commentService;

    public RestCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable long articleId) {
        return ResponseEntity.ok(commentService.findAllByArticleId(articleId));
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentResponseDto>> addNewComment(@PathVariable long articleId,
                                                                  @RequestBody CommentRequestDto commentRequestDto,
                                                                  @LoggedInUser User user) {
        commentService.addComment(commentRequestDto, articleId, user);
        return ResponseEntity.ok(commentService.findAllByArticleId(articleId));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<List<CommentResponseDto>> updateComment(@PathVariable long commentId,
                                                                  @RequestBody CommentRequestDto commentRequestDto,
                                                                  @LoggedInUser User user) {
        long articleId = commentService.update(commentId, commentRequestDto, user);
        return ResponseEntity.ok(commentService.findAllByArticleId(articleId));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<List<CommentResponseDto>> deleteComment(@PathVariable long commentId,
                                                                  @LoggedInUser User user) {
        long articleId = commentService.delete(commentId, user);
        return ResponseEntity.ok(commentService.findAllByArticleId(articleId));
    }

    @ExceptionHandler(CommenterMismatchException.class)
    public ResponseEntity<String> handleCommenterMismatchException() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
