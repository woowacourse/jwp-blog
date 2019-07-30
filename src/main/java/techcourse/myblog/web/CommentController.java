package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    private final ArticleService articleService;

    public CommentController(final CommentService commentService, final ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @GetMapping("/comment/{commentId}/edit")
    public String openCommentEdit(@PathVariable final Long commentId, final HttpSession httpSession, final Model model) {
        if (checkSession(httpSession)) return "redirect:/login";
        model.addAttribute("comment", commentService.findById(commentId));
        return "comment-edit";
    }

    @PostMapping("/articles/{articleId}/comment")
    public String create(@PathVariable final Long articleId, final CommentDto commentDto, final HttpSession httpSession) {
        if (checkSession(httpSession)) return "redirect:/login";
        commentDto.setWriter((User) httpSession.getAttribute("user"));
        commentDto.setArticle(articleService.findById(articleId));
        final Comment comment = new Comment(commentDto);
        commentService.save(comment);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/comment/{commentId}")
    public String updateComment(@PathVariable final Long commentId, final CommentDto commentDto, final HttpSession httpSession) {
        if (checkSession(httpSession)) return "redirect:/login";
        final Comment comment = commentService.update(commentId, commentDto);
        return "redirect:/articles/" + comment.getArticleId();
    }

    @DeleteMapping("/comment/{commentId}")
    public String updateComment(@PathVariable final Long commentId, final HttpSession httpSession) {
        if (checkSession(httpSession)) return "redirect:/login";
        final Long articleId = commentService.delete(commentId);
        return "redirect:/articles/" + articleId;
    }

    private boolean checkSession(final HttpSession httpSession) {
        return httpSession.getAttribute("user") == null;
    }
}