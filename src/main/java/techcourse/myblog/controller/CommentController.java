package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import techcourse.myblog.controller.argumentresolver.UserSession;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.CommentService;

@RequestMapping("/articles")
@Controller
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{articleId}/comments/{commentId}")
    public String commentEditForm(@PathVariable long articleId,
                                  @PathVariable long commentId,
                                  Model model) {
        Comment comment = commentService.find(commentId);

        model.addAttribute("articleId", articleId);
        model.addAttribute("commentId", commentId);
        model.addAttribute("comment", comment);

        return "comment-edit";
    }

    @PostMapping("/{articleId}/comments")
    public RedirectView save(@PathVariable long articleId,
                             CommentDto commentDto,
                             UserSession userSession) {
        commentService.save(commentDto, userSession.getUser(), articleId);
        return redirectToArticle(articleId);
    }

    private RedirectView redirectToArticle(@PathVariable long articleId) {
        return new RedirectView("/articles/" + articleId);
    }

    @PutMapping("/{articleId}/comments/{commentId}")
    public RedirectView put(@PathVariable long articleId,
                            @PathVariable long commentId,
                            CommentDto commentDto,
                            UserSession userSession) {
        commentService.update(commentId, commentDto, userSession.getUser());
        return redirectToArticle(articleId);
    }

    @DeleteMapping("/{articleId}/comments/{commentId}")
    public RedirectView delete(@PathVariable long articleId,
                               @PathVariable long commentId,
                               UserSession userSession) {
        commentService.delete(commentId, userSession.getUser());
        return redirectToArticle(articleId);
    }
}