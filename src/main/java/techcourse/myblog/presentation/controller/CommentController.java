package techcourse.myblog.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.CommentService;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}")
    public RedirectView create(HttpSession httpSession, CommentDto commentDto, @PathVariable Long articleId) {
        String email = (String) httpSession.getAttribute("email");

        commentService.save(commentDto, email, articleId);
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}/{commentId}")
    public RedirectView delete(HttpSession httpSession, @PathVariable Long articleId, @PathVariable Long commentId) {
        String email = (String) httpSession.getAttribute("email");

        commentService.findCommentWrtier(commentId, email);
        commentService.delete(commentId);
        return new RedirectView("/articles/" + articleId);
    }

    @PutMapping("/articles/{articleId}/comment-edit/{commentId}")
    public RedirectView update(HttpSession httpSession, CommentDto commentDto, @PathVariable Long articleId, @PathVariable Long commentId) {
        String email = (String) httpSession.getAttribute("email");

        commentService.findCommentWrtier(commentId, email);
        commentService.update(commentDto);
        return new RedirectView("/articles/" + articleId);
    }
}
