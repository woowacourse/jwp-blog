package techcourse.myblog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.support.SessionUser;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;
    private final ArticleReadService articleReadService;

    public CommentController(CommentService commentService, ArticleReadService articleReadService) {
        this.commentService = commentService;
        this.articleReadService = articleReadService;
    }

    @PostMapping
    @ResponseBody
    public Comment createComment(SessionUser loginUser, @PathVariable Long articleId, @RequestBody CommentDto commentDto) {
        log.info("Comment Create : {}", commentDto.getContents());

        Article article = articleReadService.findById(articleId);
        Comment comment = commentService.save(commentDto.toComment(loginUser.getUser(), article));

        return comment;
    }

    @PutMapping("/{commentId}")
    public RedirectView updateComment(SessionUser loginUser, @PathVariable Long commentId, @PathVariable Long articleId, CommentDto commentDto) {
        Article article = articleReadService.findById(articleId);
        commentService.modify(commentId, commentDto.toComment(loginUser.getUser(), article));

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{commentId}")
    public RedirectView removeComment(@PathVariable Long commentId, @PathVariable Long articleId, SessionUser loginUser) {
        commentService.deleteById(commentId, loginUser.getUser());

        return new RedirectView("/articles/" + articleId);
    }
}
