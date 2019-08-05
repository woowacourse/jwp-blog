package techcourse.myblog.web.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserSessionDto;

@RestController
public class CommentRestController {
    private CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public CommentResponseDto create(UserSessionDto userSessionDto, @RequestBody CommentRequestDto commentRequestDto) {
        Comment comment = commentService.save(userSessionDto, commentRequestDto);
        return commentService.toCommentResponseDto(comment);
    }
}
