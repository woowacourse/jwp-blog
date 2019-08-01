package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.comment.CommentService;
import techcourse.myblog.service.dto.comment.CommentRequest;
import techcourse.myblog.service.dto.user.UserResponse;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public String addComment(@PathVariable Long articleId, CommentRequest commentRequest, HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        commentService.save(commentRequest, user.getId(), articleId);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession session, CommentRequest commentRequest) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        if (user.getId().equals(commentService.findById(commentId).getAuthorId())) {
            commentService.update(commentRequest, commentId);
        }
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        if (user.getId().equals(commentService.findById(commentId).getAuthorId())) {
            commentService.delete(commentId);
        }
        return "redirect:/articles/" + articleId;
    }
}
