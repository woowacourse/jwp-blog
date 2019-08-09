package techcourse.myblog.presentation.controller;

import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.dto.CommentRequestDto;
import techcourse.myblog.application.dto.CommentResponseDto;
import techcourse.myblog.application.service.CommentService;

import javax.servlet.http.HttpSession;

@RequestMapping("/articles")
@RestController
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{articleId}/writing")
    public CommentResponseDto create(HttpSession httpSession, @RequestBody CommentRequestDto commentRequestDto, @PathVariable Long articleId) {
        String email = (String) httpSession.getAttribute("email");

        CommentResponseDto commentResponseDto = commentService.save(commentRequestDto, email, articleId);
        return commentResponseDto;
    }

    @DeleteMapping("/{articleId}/{commentId}")
    public CommentResponseDto delete(HttpSession httpSession, @PathVariable Long commentId) {
        String email = (String) httpSession.getAttribute("email");

        CommentResponseDto commentResponseDto = commentService.delete(commentId, email);
        return commentResponseDto;
    }

    @PutMapping("/{articleId}/comment-edit/{commentId}")
    public CommentResponseDto update(HttpSession httpSession, @RequestBody CommentRequestDto commentRequestDto, @PathVariable Long commentId) {
        String email = (String) httpSession.getAttribute("email");

        CommentResponseDto commentResponseDto = commentService.update(commentId, commentRequestDto, email);
        return commentResponseDto;
    }
}
