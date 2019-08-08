package techcourse.myblog.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.dto.CommentRequestDto;
import techcourse.myblog.dto.UserResponseDto;

import javax.validation.Valid;

@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ResponseBody
    @PostMapping("/article/{articleId}/comment")
    public ResponseEntity<Comment> saveComment(@PathVariable Long articleId, @RequestBody @Valid CommentRequestDto commentRequestDto, UserResponseDto userResponseDto) {
        Comment comment = commentService.addComment(commentRequestDto, userResponseDto, articleId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @ResponseBody
    @PutMapping("/article/{articleId}/comment/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long articleId, @PathVariable Long commentId,
                                                 @RequestBody CommentRequestDto commentRequestDto, UserResponseDto userResponseDto) {
        Comment comment = commentService.update(commentId, commentRequestDto, userResponseDto);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/article/{articleId}/comment/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, UserResponseDto userResponseDto) {
        commentService.remove(commentId, userResponseDto);

        return "redirect:/articles/" + articleId;
    }

}
