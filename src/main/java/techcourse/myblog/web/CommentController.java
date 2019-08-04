package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;

@Controller
public class CommentController {
    final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public String addComment(@PathVariable Long articleId, CommentRequestDto commentRequestDto,
                             @SessionAttribute(name = "user", required = false) User user) {
        commentService.save(commentRequestDto, user.getId(), articleId);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, CommentRequestDto commentRequestDto,
                                @SessionAttribute(name = "user", required = false) User user) {
        commentService.update(commentRequestDto, commentId, user.getId());
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId,
                                @SessionAttribute(name = "user", required = false) User user) {
        commentService.delete(commentId, user.getId());
        return "redirect:/articles/" + articleId;
    }
}
