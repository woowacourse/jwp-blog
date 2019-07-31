package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.dto.CommentDto;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;

    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @GetMapping("/comment/{commentId}/edit")
    public String openCommentEdit(@PathVariable Long commentId, Model model) {
        model.addAttribute("comment", commentService.findById(commentId));
        return "comment-edit";
    }

    @PostMapping("/articles/{articleId}/comment")
    public String create(@PathVariable Long articleId, CommentDto commentDto, HttpSession httpSession) {
        User author = (User)httpSession.getAttribute("user");
        Article article = articleService.findById(articleId);
        commentService.save(commentDto, author, article);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/comment/{commentId}")
    public String updateComment(@PathVariable Long commentId, CommentDto commentDto) {
        Comment comment = commentService.update(commentId, commentDto);
        return "redirect:/articles/" + comment.getArticleId();
    }

    @DeleteMapping("/comment/{commentId}")
    public String updateComment(@PathVariable Long commentId) {
        Long articleId = commentService.delete(commentId);
        return "redirect:/articles/" + articleId;
    }
}
