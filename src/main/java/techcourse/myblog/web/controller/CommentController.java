package techcourse.myblog.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserResponseDto;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/article/{articleId}/comment")
    public ResponseEntity<CommentResponseDto> saveComment(@PathVariable Long articleId, @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                          UserResponseDto userResponseDto) {
        CommentResponseDto commentResponseDto = commentService.addComment(commentRequestDto, userResponseDto, articleId);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    @PutMapping("/article/{articleId}/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                            UserResponseDto userResponseDto) {
        CommentResponseDto commentResponseDto = commentService.update(commentId, commentRequestDto, userResponseDto);

        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }
}
