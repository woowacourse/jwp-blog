package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.LoginUserDto;

import java.util.List;

@RestController
public class RestCommentController {
    private CommentService commentService;

    public RestCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/api/comments/{id}")
    public List<CommentResponseDto> readComments(@PathVariable("id") Long id) {
        return commentService.findCommentsByArticleId(id);
    }

    @PostMapping("/api/comments")
    @ResponseStatus(value = HttpStatus.OK)
    public void createComment(@RequestBody CommentRequestDto commentRequestDto, LoginUserDto user) {
        commentService.save(user.getId(), commentRequestDto);
    }

    @PutMapping("/api/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateComment(@PathVariable("commentId") Long commentId,
                              @RequestBody CommentRequestDto commentRequestDto, LoginUserDto user) {
        commentService.update(user.getId(), commentId, commentRequestDto);
    }

    @DeleteMapping("/api/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteComment(@PathVariable("commentId") Long commentId, LoginUserDto user) {
        commentService.delete(user.getId(), commentId);
    }
}
