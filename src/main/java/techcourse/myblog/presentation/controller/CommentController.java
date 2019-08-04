package techcourse.myblog.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.CommentService;

import javax.servlet.http.HttpSession;

@RequestMapping("/articles")
@Controller
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{articleId}")
    public RedirectView create(HttpSession httpSession, CommentDto commentDto, @PathVariable Long articleId) {
        String email = (String) httpSession.getAttribute("email");

        commentService.save(commentDto, email, articleId);
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}/{commentId}")
    public RedirectView delete(HttpSession httpSession, @PathVariable Long articleId, @PathVariable Long commentId) {
        String email = (String) httpSession.getAttribute("email");

        commentService.delete(commentId, email);
        return new RedirectView("/articles/" + articleId);
    }

    @PutMapping("/{articleId}/comment-edit/{commentId}")
    public RedirectView update(HttpSession httpSession, CommentDto commentDto, @PathVariable Long articleId, @PathVariable Long commentId) {
        String email = (String) httpSession.getAttribute("email");

        commentService.update(commentId, commentDto, email);
        return new RedirectView("/articles/" + articleId);
    }
}