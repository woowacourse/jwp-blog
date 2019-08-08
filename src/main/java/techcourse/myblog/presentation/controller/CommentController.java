package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.CommentService;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpSession;

@RestController
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public @ResponseBody CommentDto addComment(@RequestBody CommentDto commentDto, @PathVariable Long articleId, HttpSession session) {
        return commentService.save(commentDto, articleId, (User) session.getAttribute("user"));
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ModelAndView deleteComment(@PathVariable Long commentId, @PathVariable Long articleId, HttpSession session) {
        commentService.delete(commentId, (User) session.getAttribute("user"));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/articles/" + articleId));
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public @ResponseBody CommentDto updateComment(@RequestBody CommentDto commentDto, @PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId, HttpSession session) {
        return commentService.modify(commentId, commentDto, (User) session.getAttribute("user"));
    }
}