package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.CommentDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

@RequestMapping("/comments")
@SessionAttributes("user")
@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public String createComment(CommentDto commentDto, @ModelAttribute User user) {
        Long articleId = commentService.save(commentDto, user);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/{commentId}/edit")
    public String editCommentForm(@PathVariable Long commentId, Model model, @ModelAttribute User user) {
        commentService.checkOwner(commentId, user);

        Comment comment = commentService.findById(commentId);
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @DeleteMapping("/{commentId}")
    private String deleteComment(@PathVariable Long commentId, @ModelAttribute User user) {
        commentService.checkOwner(commentId, user);

        Comment comment = commentService.findById(commentId);
        Long articleId = comment.getArticle().getId();
        commentService.delete(commentId);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable Long commentId, CommentDto commentDto, @ModelAttribute User user) {
        commentService.checkOwner(commentId, user);

        Comment newComment = commentService.update(commentDto, commentId);
        Article commentedArticle = newComment.getArticle();
        return "redirect:/articles/" + commentedArticle.getId();
    }

}
