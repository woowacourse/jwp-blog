package techcourse.myblog.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserSessionDto;

@RestController
@RequestMapping("/comments")
public class CommentRestController {
    private CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponseDto create(UserSessionDto userSessionDto, @RequestBody CommentRequestDto commentRequestDto) {

        Comment comment = commentService.save(userSessionDto, commentRequestDto);
        return commentService.toCommentResponseDto(comment);
    }

    @PutMapping("/{commentId}")
    public CommentResponseDto updateComment(@PathVariable("commentId") Long commentId,
                                            @RequestBody CommentRequestDto commentRequestDto,
                                            UserSessionDto userSession) {

        Comment comment = commentService.update(userSession, commentId, commentRequestDto);
        return commentService.toCommentResponseDto(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable("commentId") Long commentId,
                                                            UserSessionDto userSession) {

        commentService.delete(userSession, commentId);
        return new ResponseEntity<>(new CommentResponseDto(commentId, null,
                null, null, null),
                HttpStatus.OK);
    }
}
