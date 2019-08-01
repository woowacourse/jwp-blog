package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.service.comment.CommentService;
import techcourse.myblog.service.dto.comment.CommentRequest;
import techcourse.myblog.service.dto.user.UserResponse;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@Controller
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public ModelAndView addComment(@PathVariable Long articleId, CommentRequest commentRequest, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        commentService.save(commentRequest, user.getId(), articleId);
        modelAndView.setView(new RedirectView("/articles/" + articleId));
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public ModelAndView updateComment(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession session, CommentRequest commentRequest) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        modelAndView.setView(new RedirectView("/articles/" + articleId));
        if (user.getId().equals(commentService.findById(commentId).getAuthorId())) {
            commentService.update(commentRequest, commentId);
        }
        return modelAndView;
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ModelAndView deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/articles/" + articleId));
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        if (user.getId().equals(commentService.findById(commentId).getAuthorId())) {
            commentService.delete(commentId);
        }
        return modelAndView;
    }
}
