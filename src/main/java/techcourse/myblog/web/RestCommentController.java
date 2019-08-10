package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.LoginUserDto;

@RestController
@RequestMapping("/api/comments")
public class RestCommentController {
    private CommentService commentService;

    public RestCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void createComment(@RequestBody CommentRequestDto commentRequestDto, LoginUserDto user) {
        commentService.save(user.getId(), commentRequestDto);
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateComment(@PathVariable("commentId") Long commentId,
                              @RequestBody CommentRequestDto commentRequestDto, LoginUserDto user) {
        commentService.update(user.getId(), commentId, commentRequestDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteComment(@PathVariable("commentId") Long commentId, LoginUserDto user) {
        commentService.delete(user.getId(), commentId);
    }
}
