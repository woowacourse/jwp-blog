package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.UserResponseDto;

@Controller
public class CommentMvcController {
    private final CommentService commentService;

    public CommentMvcController(CommentService commentService) {
        this.commentService = commentService;
    }

    @DeleteMapping("/article/{articleId}/comment/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, UserResponseDto userResponseDto) {
        commentService.remove(commentId, userResponseDto);

        return "redirect:/articles/" + articleId;
    }
}
