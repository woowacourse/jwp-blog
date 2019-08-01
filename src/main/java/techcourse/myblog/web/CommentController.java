package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.service.comment.CommentService;
import techcourse.myblog.service.dto.comment.CommentRequestDto;
import techcourse.myblog.service.dto.user.UserResponseDto;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@Controller
public class CommentController {
    final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public String addComment(@PathVariable Long articleId, CommentRequestDto commentRequestDto, HttpSession session) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        commentService.save(commentRequestDto, user.getId(), articleId);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession session, CommentRequestDto commentRequestDto) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        if (user.matchId(commentService.findById(commentId).getAuthorId())) {
            commentService.update(commentRequestDto, commentId);
        }
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession session) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        if (user.matchId(commentService.findById(commentId).getAuthorId())) {
            commentService.delete(commentId);
        }
        return "redirect:/articles/" + articleId;
    }
}
