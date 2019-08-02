package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.user.Email;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.support.UserSessionInfo;

@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comment")
    public String addComment(@PathVariable long articleId, UserSessionInfo userSessionInfo, CommentDto commentDto) {
        commentService.addComment(articleId, Email.of(userSessionInfo.getEmail()), commentDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}/comment/{commentId}")
    public String deleteComment(@PathVariable long articleId, @PathVariable long commentId, UserSessionInfo userSessionInfo) {
        commentService.deleteComment(commentId, Email.of(userSessionInfo.getEmail()));
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/articles/{articleId}/comment/{commentId}")
    public String updateComment(@PathVariable long articleId, @PathVariable long commentId, UserSessionInfo userSessionInfo, CommentDto commentDto) {
        commentService.updateComment(commentId, Email.of(userSessionInfo.getEmail()), commentDto);
        return "redirect:/articles/" + articleId;
    }
}
