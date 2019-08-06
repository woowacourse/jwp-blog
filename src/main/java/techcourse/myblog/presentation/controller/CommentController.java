package techcourse.myblog.presentation.controller;

import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.dto.CommentDto;
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
    public CommentDto create(HttpSession httpSession, @RequestBody CommentDto commentDto, @PathVariable Long articleId) {
        String email = (String) httpSession.getAttribute("email");

        return commentService.save(commentDto, email, articleId);
    }

    @DeleteMapping("/{articleId}/{commentId}")
    public String delete(HttpSession httpSession, @PathVariable Long articleId, @PathVariable Long commentId) {
        String email = (String) httpSession.getAttribute("email");

        commentService.delete(commentId, email);
        return "{\"commentId\":\"" + commentId + "\"}";
    }

    @PutMapping("/{articleId}/comment-edit/{commentId}")
    public String update(HttpSession httpSession, CommentDto commentDto, @PathVariable Long articleId, @PathVariable Long commentId) {
        String email = (String) httpSession.getAttribute("email");

        commentService.update(commentId, commentDto, email);
        return "{\"editedContents\":\"" + commentDto.getContents() + "\"}";
    }
}
