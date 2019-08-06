package techcourse.myblog.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.service.CommentService;
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
    @PostMapping("/comment/{articleId}")
    public ResponseEntity<Comment> saveComment(@PathVariable Long articleId, @RequestBody @Valid CommentRequestDto commentRequestDto, UserResponseDto userResponseDto) {
        Comment comment = commentService.addComment(commentRequestDto, userResponseDto, articleId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @ResponseBody
    @PutMapping("/comment/{articleId}/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long articleId, @PathVariable Long commentId,
                                                 @RequestBody CommentRequestDto commentRequestDto, UserResponseDto userResponseDto) {
        Comment comment = commentService.update(commentId, commentRequestDto, userResponseDto);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/comment/{articleId}/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, UserResponseDto userResponseDto) {
        commentService.remove(commentId, userResponseDto);

        return "redirect:/articles/" + articleId;
    }

}
