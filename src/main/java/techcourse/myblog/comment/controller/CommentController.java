package techcourse.myblog.comment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping("/comment/{articleId}")
    public String saveComment(@PathVariable Long articleId, @Valid CommentRequestDto commentRequestDto, UserResponseDto userResponseDto) {
        commentService.addComment(commentRequestDto, userResponseDto, articleId);

        return "redirect:/articles/" + articleId;
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
