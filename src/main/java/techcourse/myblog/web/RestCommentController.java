package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;

import java.net.URI;
import java.util.List;

@RestController
public class RestCommentController {
    private CommentService commentService;

    public RestCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments/{articleId}")
    public ResponseEntity<List<CommentResponseDto>> readComments(@PathVariable("articleId") Long articleId) {
        return ResponseEntity.ok(commentService.findCommentsByArticleId(articleId));
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequestDto commentRequestDto,
                              @LoggedUser UserPublicInfoDto userPublicInfoDto) {
        Long userId = userPublicInfoDto.getId();
        Comment comment = commentService.save(userId, commentRequestDto);
        return ResponseEntity.created(URI.create("/comments/" + comment.getArticleId())).build();
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity updateComment(@PathVariable("commentId") Long commentId,
                                                    @RequestBody CommentRequestDto commentRequestDto,
                                                    @LoggedUser UserPublicInfoDto userPublicInfoDto) {
        Long userId = userPublicInfoDto.getId();
        commentService.update(userId, commentId, commentRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId,
                              @LoggedUser UserPublicInfoDto userPublicInfoDto) {
        Long userId = userPublicInfoDto.getId();
        commentService.delete(userId, commentId);
        return ResponseEntity.ok().build();

    }

}
