package techcourse.myblog.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.annotation.EmailAnnot;
import techcourse.myblog.application.service.CommentService;
import techcourse.myblog.domain.Email;
import techcourse.myblog.domain.vo.CommentContents;

@Controller
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public RedirectView addComment(CommentContents contents, @PathVariable Long articleId, @EmailAnnot Email email) {
        commentService.save(contents, articleId, email);
        RedirectView redirectView = new RedirectView("/articles/" + articleId);
        return redirectView;
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ModelAndView deleteComment(@PathVariable Long commentId, @PathVariable Long articleId, @EmailAnnot Email email) {
        commentService.checkAuthor(commentId, email.getEmail());
        commentService.delete(commentId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/articles/"+articleId));
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public RedirectView updateComment(CommentContents commentContents, @PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId, @EmailAnnot Email email) {
        commentService.checkAuthor(commentId, email.getEmail());
        commentService.modify(commentId, commentContents);
        return new RedirectView("/articles/"+articleId);
    }
}
