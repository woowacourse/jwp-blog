package techcourse.myblog.comment.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.dto.CommentRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.comment.service.CommentService;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.utils.session.SessionContext.USER;

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

    @PutMapping("/comment/{articleId}/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, CommentRequestDto commentRequestDto, UserResponseDto userResponseDto) {
        commentService.update(commentId, commentRequestDto, userResponseDto);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/comment/{articleId}/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, UserResponseDto userResponseDto) {
        commentService.remove(commentId, userResponseDto);

        return "redirect:/articles/" + articleId;
    }

}
